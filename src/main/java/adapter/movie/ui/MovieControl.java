package adapter.movie.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
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

	public MovieControl(JDialog dialog, JLabel dialogLabel) {
		service = new MovieService(new MovieDAO());
		columnNames = new Vector<>();
		columnNames.add("MovieID"); columnNames.add("제목");
		columnNames.add("장르");    columnNames.add("감독"); columnNames.add("관람등급");
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	public void setTable(JTable t)          { this.table = t; }
	public void setMovieInsFrm(MovieInsFrm f) { this.movieInsFrm = f; }
	public void setMovieUpFrm(MovieUpFrm f)   { this.movieUpFrm = f; }

	private void dialogOpen(String msg) { dialogLabel.setText(msg); dialog.setVisible(true); }

	private void readAll() {
		try { movieList = service.findAll(); }
		catch (SQLException e) { movieList = new ArrayList<>(); dialogOpen("영화 목록 조회 실패"); }

		Vector<Vector<String>> data = new Vector<>();
		for (Movie m : movieList) {
			Vector<String> row = new Vector<>();
			row.add(String.valueOf(m.getMovieId())); row.add(m.getTitle());
			row.add(m.getGenre()); row.add(m.getDirector()); row.add(m.getRating());
			data.add(row);
		}
		table.setModel(new DefaultTableModel(data, columnNames));
	}

	private void insertOne() {
		Movie movie = new Movie(0, movieInsFrm.tfTitle.getText(), movieInsFrm.tfGenre.getText(),
				movieInsFrm.tfDirector.getText(), movieInsFrm.tfRating.getText());
		try {
			service.save(movie);
			movieInsFrm.tfTitle.setText(""); movieInsFrm.tfGenre.setText("");
			movieInsFrm.tfDirector.setText(""); movieInsFrm.tfRating.setText("");
			movieInsFrm.setVisible(false);
			readAll();
		} catch (SQLException e) { dialogOpen("영화 추가 실패"); }
	}

	private void updateOne() {
		Movie movie = new Movie(selectedMovieId, movieUpFrm.tfTitle.getText(), movieUpFrm.tfGenre.getText(),
				movieUpFrm.tfDirector.getText(), movieUpFrm.tfRating.getText());
		try { service.update(movie); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("영화 수정 실패"); }
	}

	private void deleteOne() {
		try { service.delete(selectedMovieId); clearUpFrm(); readAll(); }
		catch (SQLException e) { dialogOpen("영화 삭제 실패"); }
	}

	private void clearUpFrm() {
		movieUpFrm.tfTitle.setText(""); movieUpFrm.tfGenre.setText("");
		movieUpFrm.tfDirector.setText(""); movieUpFrm.tfRating.setText("");
		movieUpFrm.setVisible(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "목록 조회": readAll(); break;
			case "영화 추가": movieInsFrm.setVisible(true); break;
			case "저장":      insertOne(); break;
			case "취소":      movieInsFrm.setVisible(false); movieUpFrm.setVisible(false); break;
			case "수정":      updateOne(); break;
			case "삭제":      deleteOne(); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Movie m = movieList.get(table.getSelectedRow());
		selectedMovieId = m.getMovieId();
		movieUpFrm.tfTitle.setText(m.getTitle()); movieUpFrm.tfGenre.setText(m.getGenre());
		movieUpFrm.tfDirector.setText(m.getDirector()); movieUpFrm.tfRating.setText(m.getRating());
		movieUpFrm.setVisible(true);
	}

}
