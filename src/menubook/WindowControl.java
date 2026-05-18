package menubook;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JLabel;

import menubook.util.DBUtil;

public class WindowControl extends WindowAdapter {

	private JDialog dialog;
	private JLabel dialogLabel;

	public WindowControl( JDialog dialog, JLabel dialogLabel ) {
		this.dialog = dialog;
		this.dialogLabel = dialogLabel;
	}

	@Override
	public void windowClosing(WindowEvent e) {

		try {
			DBUtil.closeConnection();
		} catch (SQLException e1) {
			dialogLabel.setText( "커넥션 종료 실패" );
			dialog.setVisible(true);
		} finally {
			System.exit(0);
		}

	} // windowClosing

} // class
