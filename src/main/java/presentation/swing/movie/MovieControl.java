package presentation.swing.movie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import adapter.movie.MovieDAO;
import domain.movie.Movie;
import domain.movie.MovieService;

public class MovieControl extends MouseAdapter implements ActionListener {

	private final MovieService service;
	private List<Movie> movieList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;
	private MovieInsFrm movieInsFrm;
	private MovieUpFrm movieUpFrm;
	private int selectedMovieId;
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";

	public MovieControl(JDialog dialog, JLabel dialogLabel) {
		service = new MovieService(new MovieDAO());
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("MovieID"); columnNames.add("제목");
		columnNames.add("장르"); columnNames.add("감독"); columnNames.add("관람등급"); columnNames.add("러닝타임(분)");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)          { this.table = t; }
	public void setMovieInsFrm(MovieInsFrm f) { this.movieInsFrm = f; }
	public void setMovieUpFrm(MovieUpFrm f)   { this.movieUpFrm = f; }
	public void setDeleteBtn(JButton btn)   { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)   { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }
	public void search(String keyword) { lastKeyword = keyword.trim(); readAll(); }
	public int getMovieCount() { return movieList.size(); }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { movieList = service.findAll(); }
		catch (SQLException e) { movieList = new ArrayList<>(); dialogOpen("영화 목록 조회 실패"); }

		String keyword = lastKeyword.toLowerCase();
		Vector<Vector<Object>> data = new Vector<>();
		for (Movie m : movieList) {
			// 검색어가 있을 경우 제목이나 장르에 포함되지 않으면 제외
			if (!keyword.isEmpty() && !m.getTitle().toLowerCase().contains(keyword) && 
				!m.getGenre().toLowerCase().contains(keyword)) continue;

			Vector<Object> row = new Vector<>();
			row.add(Boolean.FALSE); // 체크박스
			row.add(String.valueOf(m.getMovieId())); row.add(m.getTitle());
			row.add(m.getGenre()); row.add(m.getDirector()); row.add(m.getRating());
			row.add(String.valueOf(m.getRuntime()));
			data.add(row);
		}
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			@Override public Class<?> getColumnClass(int col) { return col == 0 ? Boolean.class : String.class; }
			@Override public boolean isCellEditable(int row, int col) { return false; }
		};
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);

		model.addTableModelListener(e -> {
			int checkCount = 0;
			for (int i = 0; i < table.getRowCount(); i++) {
				if ((Boolean) table.getValueAt(i, 0)) checkCount++;
			}
			if (btnDeleteTop != null) btnDeleteTop.setEnabled(checkCount > 0);
			if (btnUpdateBottom != null) btnUpdateBottom.setEnabled(checkCount == 1);
		});
		if (btnDeleteTop != null) btnDeleteTop.setEnabled(false);
		if (btnUpdateBottom != null) btnUpdateBottom.setEnabled(false);
	}

	private void insertOne() {
		int runtime = parseRuntime(movieInsFrm.tfRuntime.getText());
		Movie movie = new Movie(0, movieInsFrm.tfTitle.getText(), movieInsFrm.tfGenre.getText(),
				movieInsFrm.tfDirector.getText(), movieInsFrm.tfRating.getText(), runtime);
		try {
			service.save(movie);
			movieInsFrm.tfTitle.setText(""); movieInsFrm.tfGenre.setText("");
			movieInsFrm.tfDirector.setText(""); movieInsFrm.tfRating.setText(""); movieInsFrm.tfRuntime.setText("");
			movieInsFrm.setVisible(false);
			readAll();
		} catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "영화 추가 실패"); }
	}

	private void updateOne() {
		int runtime = parseRuntime(movieUpFrm.tfRuntime.getText());
		Movie movie = new Movie(selectedMovieId, movieUpFrm.tfTitle.getText(), movieUpFrm.tfGenre.getText(),
				movieUpFrm.tfDirector.getText(), movieUpFrm.tfRating.getText(), runtime);
		try { service.update(movie); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "영화 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedMovieId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("영화 삭제 실패"); }
	}

	private void clearUpFrm() {
		movieUpFrm.tfTitle.setText(""); movieUpFrm.tfGenre.setText("");
		movieUpFrm.tfDirector.setText(""); movieUpFrm.tfRating.setText(""); movieUpFrm.tfRuntime.setText("");
		movieUpFrm.setVisible(false);
	}

	private int parseRuntime(String text) {
		try { return Integer.parseInt(text.trim()); } catch (NumberFormatException e) { return 0; }
	}

	private void openUpdateFrmChecked() {
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				selectedMovieId = Integer.parseInt(table.getValueAt(i, 1).toString());
				Movie m = movieList.stream().filter(item -> item.getMovieId() == selectedMovieId).findFirst().orElse(null);
				if (m != null) {
					movieUpFrm.tfTitle.setText(m.getTitle()); movieUpFrm.tfGenre.setText(m.getGenre());
					movieUpFrm.tfDirector.setText(m.getDirector()); movieUpFrm.tfRating.setText(m.getRating());
					movieUpFrm.tfRuntime.setText(String.valueOf(m.getRuntime()));
					movieUpFrm.setVisible(true);
				}
				break;
			}
		}
	}

	private void deleteChecked() {
		int opt = JOptionPane.showConfirmDialog(null, "선택한 영화 정보를 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
		if (opt != JOptionPane.YES_OPTION) return;

		int count = 0;
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				int id = Integer.parseInt(table.getValueAt(i, 1).toString());
				try { service.delete(id); count++; } catch (SQLException ignored) {}
			}
		}
		if (count > 0) { dialogOpen(count + "건의 영화 정보가 삭제되었습니다."); readAll(); }
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (!"목록 조회".equals(cmd)) {
			switch (cmd) {
			case "영화 추가": movieInsFrm.setVisible(true); break;
			case "저장":      insertOne(); break;
			case "취소":      movieInsFrm.setVisible(false); movieUpFrm.setVisible(false); break;
			case "수정":      if (e.getSource() == btnUpdateBottom) openUpdateFrmChecked(); else updateOne(); break;
			case "삭제":      deleteChecked(); break;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = table.columnAtPoint(e.getPoint());
		int row = table.rowAtPoint(e.getPoint());
		if (col == 0 && row >= 0) {
			boolean curr = (Boolean) table.getValueAt(row, 0);
			table.setValueAt(!curr, row, 0);
		}
	}

}
