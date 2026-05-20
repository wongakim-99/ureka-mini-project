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

import domain.movie.Movie;
import domain.movie.MovieService;
import infrastructure.AppLogger;
import java.util.logging.Logger;
import presentation.swing.maindashboard.DashboardMenuRouter;

public class MovieController extends MouseAdapter implements ActionListener {

	private static final Logger log = AppLogger.get(MovieController.class);
	private final MovieService service;
	private List<Movie> movieList = new ArrayList<>();
	private final Vector<String> columnNames;

	private JDialog dialog;
	private JLabel dialogLabel;
	private JTable table;
	private MovieCreateFrame movieCreateFrame;
	private MovieUpdateFrame movieUpdateFrame;
	private int selectedMovieId;
	private JButton btnDeleteTop, btnUpdateBottom;
	private String lastKeyword = "";
	private DashboardMenuRouter router;

	public MovieController(MovieService service, JDialog dialog, JLabel dialogLabel) {
		this.service = service;
		columnNames = new Vector<>();
		columnNames.add("선택"); columnNames.add("MovieID"); columnNames.add("제목");
		columnNames.add("장르"); columnNames.add("감독"); columnNames.add("관람등급"); columnNames.add("러닝타임(분)");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)          { this.table = t; }
	public void setMovieCreateFrame(MovieCreateFrame f) { this.movieCreateFrame = f; }
	public void setMovieUpdateFrame(MovieUpdateFrame f)   { this.movieUpdateFrame = f; }
	public void setDeleteBtn(JButton btn)   { this.btnDeleteTop = btn; }
	public void setUpdateBtn(JButton btn)   { this.btnUpdateBottom = btn; }
	public void load() { lastKeyword = ""; readAll(); }
	public void search(String keyword) { lastKeyword = keyword.trim(); readAll(); }
	public int getMovieCount() { return movieList.size(); }
	public void setRouter(DashboardMenuRouter router) { this.router = router; }
	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		long t = System.currentTimeMillis();
		try { movieList = service.findAll(); }
		catch (SQLException e) { movieList = new ArrayList<>(); dialogOpen("영화 목록 조회 실패"); }
		log.fine(String.format("DB 조회 완료 (%d건, %dms)", movieList.size(), System.currentTimeMillis() - t));

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
		int runtime = parseRuntime(movieCreateFrame.tfRuntime.getText());
		Movie movie = new Movie(0, movieCreateFrame.tfTitle.getText(), movieCreateFrame.tfGenre.getText(),
				movieCreateFrame.tfDirector.getText(), movieCreateFrame.tfRating.getText(), runtime);
		try {
			service.save(movie);
			movieCreateFrame.tfTitle.setText(""); movieCreateFrame.tfGenre.setText("");
			movieCreateFrame.tfDirector.setText(""); movieCreateFrame.tfRating.setText(""); movieCreateFrame.tfRuntime.setText("");
			movieCreateFrame.setVisible(false);
			readAll();
			if (router != null) {
			    router.updateMovieCount();
			}
		} catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "영화 추가 실패"); }
	}

	private void updateOne() {
		int runtime = parseRuntime(movieUpdateFrame.tfRuntime.getText());
		Movie movie = new Movie(selectedMovieId, movieUpdateFrame.tfTitle.getText(), movieUpdateFrame.tfGenre.getText(),
				movieUpdateFrame.tfDirector.getText(), movieUpdateFrame.tfRating.getText(), runtime);
		try { service.update(movie); clearUpdateFrame(); readAll(); }
		catch (SQLException e) { dialogOpen(e.getMessage() != null ? e.getMessage() : "영화 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedMovieId); clearUpdateFrame(); readAll(); }
		catch (SQLException e) { dialogOpen("영화 삭제 실패"); }
	}

	private void clearUpdateFrame() {
		movieUpdateFrame.tfTitle.setText(""); movieUpdateFrame.tfGenre.setText("");
		movieUpdateFrame.tfDirector.setText(""); movieUpdateFrame.tfRating.setText(""); movieUpdateFrame.tfRuntime.setText("");
		movieUpdateFrame.setVisible(false);
	}

	private int parseRuntime(String text) {
		try { return Integer.parseInt(text.trim()); } catch (NumberFormatException e) { return 0; }
	}

	private void openCheckedUpdateFrame() {
		for (int i = 0; i < table.getRowCount(); i++) {
			if ((Boolean) table.getValueAt(i, 0)) {
				selectedMovieId = Integer.parseInt(table.getValueAt(i, 1).toString());
				Movie m = movieList.stream().filter(item -> item.getMovieId() == selectedMovieId).findFirst().orElse(null);
				if (m != null) {
					movieUpdateFrame.tfTitle.setText(m.getTitle()); movieUpdateFrame.tfGenre.setText(m.getGenre());
					movieUpdateFrame.tfDirector.setText(m.getDirector()); movieUpdateFrame.tfRating.setText(m.getRating());
					movieUpdateFrame.tfRuntime.setText(String.valueOf(m.getRuntime()));
					movieUpdateFrame.setVisible(true);
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
		
		
		if (count > 0) { dialogOpen(count + "건의 영화 정보가 삭제되었습니다."); readAll(); 
			if (router != null) {
			    router.updateMovieCount();
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (!"목록 조회".equals(cmd)) {
			switch (cmd) {
			case "영화 추가": movieCreateFrame.setVisible(true); break;
			case "저장":      insertOne(); break;
			case "취소":      movieCreateFrame.setVisible(false); movieUpdateFrame.setVisible(false); break;
			case "수정":      if (e.getSource() == btnUpdateBottom) openCheckedUpdateFrame(); else updateOne(); break;
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
