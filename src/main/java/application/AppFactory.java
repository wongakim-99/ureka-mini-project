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
import presentation.swing.customer.CustControl;
import presentation.swing.movie.MovieControl;
import presentation.swing.reservation.ReservControl;
import presentation.swing.revenue.RevenueControl;
import presentation.swing.screening.ScreeningControl;
import presentation.swing.theater.TheaterControl;

public final class AppFactory {

	private AppFactory() {}

	public static MovieControl createMovieControl(JDialog dialog, JLabel dialogLabel) {
		return new MovieControl(new MovieService(new MovieJdbcRepository()), dialog, dialogLabel);
	}

	public static ReservControl createReservationControl(JDialog dialog, JLabel dialogLabel) {
		return new ReservControl(new ReservationService(new ReservationJdbcRepository()), dialog, dialogLabel);
	}

	public static ScreeningControl createScreeningControl(JDialog dialog, JLabel dialogLabel) {
		return new ScreeningControl(new ScreeningService(new ScreeningJdbcRepository()), dialog, dialogLabel);
	}

	public static CustControl createCustomerControl(JDialog dialog, JLabel dialogLabel) {
		return new CustControl(new CustomerService(new CustomerJdbcRepository()), dialog, dialogLabel);
	}

	public static TheaterControl createTheaterControl(JDialog dialog, JLabel dialogLabel) {
		return new TheaterControl(new TheaterService(new TheaterJdbcRepository()), dialog, dialogLabel);
	}

	public static RevenueControl createRevenueControl(JDialog dialog, JLabel dialogLabel) {
		return new RevenueControl(new RevenueService(new RevenueJdbcRepository()), dialog, dialogLabel);
	}
}
