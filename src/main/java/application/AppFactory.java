package application;

import javax.swing.JDialog;
import javax.swing.JLabel;

import domain.customer.CustomerService;
import domain.movie.MovieService;
import domain.reservation.ReservationService;
import domain.revenue.RevenueService;
import domain.screening.ScreeningService;
import domain.theater.TheaterService;
import infrastructure.persistence.customer.CustomerJdbcRepository;
import infrastructure.persistence.movie.MovieJdbcRepository;
import infrastructure.persistence.reservation.ReservationJdbcRepository;
import infrastructure.persistence.revenue.RevenueJdbcRepository;
import infrastructure.persistence.screening.ScreeningJdbcRepository;
import infrastructure.persistence.theater.TheaterJdbcRepository;
import presentation.swing.customer.CustomerController;
import presentation.swing.movie.MovieController;
import presentation.swing.reservation.ReservationController;
import presentation.swing.revenue.RevenueController;
import presentation.swing.screening.ScreeningController;
import presentation.swing.theater.TheaterController;

public final class AppFactory {

	private AppFactory() {}

	public static MovieController createMovieController(JDialog dialog, JLabel dialogLabel) {
		return new MovieController(new MovieService(new MovieJdbcRepository()), dialog, dialogLabel);
	}

	public static ReservationController createReservationController(JDialog dialog, JLabel dialogLabel) {
		return new ReservationController(new ReservationService(new ReservationJdbcRepository()), dialog, dialogLabel);
	}

	public static ScreeningController createScreeningController(JDialog dialog, JLabel dialogLabel) {
		return new ScreeningController(new ScreeningService(new ScreeningJdbcRepository()), dialog, dialogLabel);
	}

	public static CustomerController createCustomerController(JDialog dialog, JLabel dialogLabel) {
		return new CustomerController(new CustomerService(new CustomerJdbcRepository()), dialog, dialogLabel);
	}

	public static TheaterController createTheaterController(JDialog dialog, JLabel dialogLabel) {
		return new TheaterController(new TheaterService(new TheaterJdbcRepository()), dialog, dialogLabel);
	}

	public static RevenueController createRevenueController(JDialog dialog, JLabel dialogLabel) {
		return new RevenueController(new RevenueService(new RevenueJdbcRepository()), dialog, dialogLabel);
	}
}
