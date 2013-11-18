package net.trevize.galatee;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.MatteBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//import net.trevize.tinker.CellStyle;
//import net.trevize.tinker.XGridBag;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PreferencesDialog.java - Sep 6, 2010
 */

public class PreferencesDialog extends JDialog implements WindowListener,
		ActionListener, ChangeListener {

	private Galatee galatee;
	private int image_width;
	private int image_height;
	private int description_width;

	private JSpinner spinner_image_width;
	private JSpinner spinner_image_height;
	private JSpinner spinner_description_width;

	public static final String ACTION_COMMAND_UPDATE_PREFERENCES = "ACTION_COMMAND_UPDATE_PREFERENCES";

	public PreferencesDialog(Galatee galatee) {
		this.galatee = galatee;
		this.image_width = GalateeProperties.getImage_width();
		this.image_height = GalateeProperties.getImage_height();
		this.description_width = GalateeProperties.getDescription_width();

		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Preferences");
		init();
		pack();
		setLocationRelativeTo(galatee);
	}

	private void init() {
		setLayout(new GridBagLayout());
		XGridBag xgb = new XGridBag(this);

		JLabel header = new JLabel();
		header.setOpaque(true);
		header.setBackground(Color.WHITE);
		header.setBorder(new MatteBorder(0, 15, 0, 0, Color.BLUE));
		header.setText("<html><body><h3> Display preferences</h3></body></html>");
		CellStyle style0 = new CellStyle(1., 0., GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(header, style0, 0, 0, 1, 2);

		JLabel label_image_width = new JLabel("image width: ");
		CellStyle style1 = new CellStyle(0., 0., GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(label_image_width, style1, 1, 0);
		SpinnerModel spinner_model_image_width = new SpinnerNumberModel(
				image_width, 16, 512, 1);
		spinner_image_width = new JSpinner(spinner_model_image_width);
		spinner_image_width.addChangeListener(this);
		xgb.add(spinner_image_width, style1, 1, 1);

		JLabel label_image_height = new JLabel("image height: ");
		xgb.add(label_image_height, style1, 2, 0);
		SpinnerModel spinner_model_image_height = new SpinnerNumberModel(
				image_height, 16, 512, 1);
		spinner_image_height = new JSpinner(spinner_model_image_height);
		spinner_image_height.addChangeListener(this);
		xgb.add(spinner_image_height, style1, 2, 1);

		JLabel label_description_width = new JLabel("description width: ");
		xgb.add(label_description_width, style1, 3, 0);
		SpinnerModel spinner_model_description_width = new SpinnerNumberModel(
				description_width, 0, 512, 1);
		spinner_description_width = new JSpinner(
				spinner_model_description_width);
		spinner_description_width.addChangeListener(this);
		xgb.add(spinner_description_width, style1, 3, 1);

		xgb.add(new JSeparator(), style0, 4, 0, 1, 2);

		JButton button_update = new JButton("update preferences");
		button_update
				.setActionCommand(PreferencesDialog.ACTION_COMMAND_UPDATE_PREFERENCES);
		button_update.addActionListener(this);
		xgb.add(button_update, style0, 5, 0, 1, 2);
	}

	/***************************************************************************
	 * implementation of WindowListener.
	 **************************************************************************/

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();
		if (action_command
				.equals(PreferencesDialog.ACTION_COMMAND_UPDATE_PREFERENCES)) {
			//saving the new preferences.
			GalateeProperties.setImage_width(image_width);
			GalateeProperties.setImage_height(image_height);
			GalateeProperties.setDescription_width(description_width);
			GalateeProperties.saveProperties();
			
			//updating the current display.
			galatee.updateGItemDimension(new Dimension(image_width,
					image_height), description_width);
			galatee.getTable().repaint();
		}
	}

	/***************************************************************************
	 * implementation of ChangeListener.
	 **************************************************************************/

	@Override
	public void stateChanged(ChangeEvent e) {
		JSpinner spinner = (JSpinner) e.getSource();
		int value = (Integer) spinner.getValue();
		if (spinner == spinner_image_width) {
			image_width = value;
		} else if (spinner == spinner_image_height) {
			image_height = value;
		} else if (spinner == spinner_description_width) {
			description_width = value;
		}
	}

}
