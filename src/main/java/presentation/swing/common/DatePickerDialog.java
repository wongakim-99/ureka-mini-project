package presentation.swing.common;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerDialog extends JDialog {

    private int year, month;
    private String result;
    private JLabel lblTitle;
    private JPanel gridPanel;

    private DatePickerDialog(Window owner, String currentDate) {
        super(owner, "날짜 선택", ModalityType.APPLICATION_MODAL);
        Calendar cal = Calendar.getInstance();
        try { cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(currentDate)); }
        catch (Exception ignored) {}
        year  = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        result = null;
        build();
        pack();
    }

    public static String show(Component parent, String currentDate) {
        Window owner = SwingUtilities.getWindowAncestor(parent);
        DatePickerDialog dlg = new DatePickerDialog(owner, currentDate);
        dlg.setLocationRelativeTo(parent);
        dlg.setVisible(true);
        return dlg.result;
    }

    private void build() {
        setLayout(new BorderLayout(4, 4));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));

        JPanel nav = new JPanel(new BorderLayout());
        JButton prev = new JButton("◀");
        JButton next = new JButton("▶");
        lblTitle = new JLabel("", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Malgun Gothic", Font.BOLD, 13));
        prev.setPreferredSize(new Dimension(40, 28));
        next.setPreferredSize(new Dimension(40, 28));
        nav.add(prev, BorderLayout.WEST);
        nav.add(lblTitle, BorderLayout.CENTER);
        nav.add(next, BorderLayout.EAST);
        add(nav, BorderLayout.NORTH);

        gridPanel = new JPanel();
        add(gridPanel, BorderLayout.CENTER);

        JButton today = new JButton("오늘");
        today.setFont(new Font("Malgun Gothic", Font.PLAIN, 12));
        today.addActionListener(e -> {
            result = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            dispose();
        });
        add(today, BorderLayout.SOUTH);

        prev.addActionListener(e -> { if (--month < 0)  { month = 11; year--; } refresh(); });
        next.addActionListener(e -> { if (++month > 11) { month = 0;  year++; } refresh(); });

        refresh();
    }

    private void refresh() {
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(0, 7, 3, 3));

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        lblTitle.setText(year + "년 " + (month + 1) + "월");

        String[] headers = {"일", "월", "화", "수", "목", "금", "토"};
        Color[] hColors   = {Color.RED, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK, new Color(0, 80, 200)};
        for (int i = 0; i < 7; i++) {
            JLabel h = new JLabel(headers[i], SwingConstants.CENTER);
            h.setFont(new Font("Malgun Gothic", Font.BOLD, 11));
            h.setForeground(hColors[i]);
            gridPanel.add(h);
        }

        int firstDow = cal.get(Calendar.DAY_OF_WEEK) - 1;
        for (int i = 0; i < firstDow; i++) gridPanel.add(new JLabel());

        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int d = 1; d <= maxDay; d++) {
            final int day = d;
            JButton btn = new JButton(String.valueOf(d));
            btn.setFont(new Font("Malgun Gothic", Font.PLAIN, 11));
            btn.setMargin(new Insets(2, 2, 2, 2));
            btn.setPreferredSize(new Dimension(36, 28));
            int col = (firstDow + d - 1) % 7;
            if (col == 0) btn.setForeground(Color.RED);
            else if (col == 6) btn.setForeground(new Color(0, 80, 200));
            btn.addActionListener(e -> {
                result = String.format("%04d-%02d-%02d", year, month + 1, day);
                dispose();
            });
            gridPanel.add(btn);
        }

        gridPanel.revalidate();
        gridPanel.repaint();
        pack();
    }
}
