package presentation.swing;

import javax.swing.JDialog;
import javax.swing.JLabel;

import application.AppFactory;
import presentation.swing.customer.CustomerController;
import presentation.swing.customer.CustomerCreateFrame;
import presentation.swing.customer.CustomerUpdateFrame;
import presentation.swing.movie.MovieController;
import presentation.swing.movie.MovieCreateFrame;
import presentation.swing.movie.MovieUpdateFrame;
import presentation.swing.reservation.ReservationController;
import presentation.swing.reservation.ReservationCreateFrame;
import presentation.swing.reservation.ReservationUpdateFrame;
import presentation.swing.revenue.RevenueController;
import presentation.swing.screening.ScreeningController;
import presentation.swing.screening.ScreeningCreateFrame;
import presentation.swing.screening.ScreeningUpdateFrame;

final class DashboardControls {

	final MovieController movieController;
	final MovieCreateFrame movieCreateFrame;
	final MovieUpdateFrame movieUpdateFrame;
	final ReservationController reservationController;
	final ReservationCreateFrame reservationCreateFrame;
	final ReservationUpdateFrame reservationUpdateFrame;
	final ScreeningController screeningController;
	final ScreeningCreateFrame screeningCreateFrame;
	final ScreeningUpdateFrame screeningUpdateFrame;
	final CustomerController customerController;
	final CustomerCreateFrame customerCreateFrame;
	final CustomerUpdateFrame customerUpdateFrame;
	final RevenueController revenueController;

	DashboardControls(JDialog dialog, JLabel dialogLabel) {
		movieCreateFrame = new MovieCreateFrame();
		movieUpdateFrame = new MovieUpdateFrame();
		movieController = AppFactory.createMovieController(dialog, dialogLabel);
		movieCreateFrame.addEvent(movieController);
		movieUpdateFrame.addEvent(movieController);

		reservationCreateFrame = new ReservationCreateFrame();
		reservationUpdateFrame = new ReservationUpdateFrame();
		reservationController = AppFactory.createReservationController(dialog, dialogLabel);
		reservationCreateFrame.addEvent(reservationController);
		reservationUpdateFrame.addEvent(reservationController);

		screeningCreateFrame = new ScreeningCreateFrame();
		screeningUpdateFrame = new ScreeningUpdateFrame();
		screeningController = AppFactory.createScreeningController(dialog, dialogLabel);
		screeningCreateFrame.addEvent(screeningController);
		screeningUpdateFrame.addEvent(screeningController);

		customerCreateFrame = new CustomerCreateFrame();
		customerUpdateFrame = new CustomerUpdateFrame();
		customerController = AppFactory.createCustomerController(dialog, dialogLabel);
		customerCreateFrame.addEvent(customerController);
		customerUpdateFrame.addEvent(customerController);

		revenueController = AppFactory.createRevenueController(dialog, dialogLabel);
	}
}
