package presentation.swing.reservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SeatSelectionDialog extends JDialog {

    private final int rows; // number of rows (A..)
    private final int cols; // total columns
    private final Set<String> occupied;
    private final List<String> selected = new ArrayList<>();
    private final int maxSelect;
    private JLabel counterLabel;

    public SeatSelectionDialog(java.awt.Frame owner, int rows, int cols, Set<String> occupied, int maxSelect) {
        super(owner, "좌석 선택", true);
        this.rows = rows; this.cols = cols; this.occupied = occupied == null ? new HashSet<>() : occupied;
        this.maxSelect = Math.max(1, maxSelect);
        initUi();
    }

    private void initUi() {
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.setBorder(new EmptyBorder(8, 12, 8, 12));
        JLabel screen = new JLabel("SCREEN");
        screen.setFont(new Font("Malgun Gothic", Font.BOLD, 16));
        screen.setForeground(new Color(80,80,80));
        top.add(screen);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(new EmptyBorder(6,12,6,12));

        // fixed blocks per design: left 4, center 10, right 2 (total 16)
        int leftBlock = 4;
        int centerBlock = 10;
        int rightBlock = 2;
        if (cols != 16) {
            centerBlock = Math.max(1, cols - leftBlock - rightBlock);
        }

        int gapCols = 2; // one-gap between left/center and center/right
        int gridCols = 1 + leftBlock + 1 + centerBlock + 1 + rightBlock + 1; // leftLabel + left + gap + center + gap + right + rightLabel

        // use 2px gaps as requested
        int cellGap = 2;
        JPanel grid = new JPanel(new GridLayout(rows, gridCols, cellGap, cellGap));

        for (int r = 0; r < rows; r++) {
            // left label
            JLabel ll = new JLabel(String.valueOf((char)('A' + r)), JLabel.CENTER);
            ll.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            ll.setForeground(new Color(100,100,100));
            grid.add(ll);

            int globalCol = 1;
            // Special case: if this is the last row (J) only place center block (cols 5..14)
            boolean lastRowCenterOnly = (r == rows - 1);

            // left block seats (or placeholders on last row)
            for (int c = 0; c < leftBlock; c++) {
                if (lastRowCenterOnly) {
                    JPanel placeholder = new JPanel();
                    placeholder.setOpaque(false);
                    grid.add(placeholder);
                } else {
                    String seatId = String.valueOf((char)('A' + r)) + (globalCol);
                    JToggleButton btn = createSeatButton(seatId, globalCol);
                    grid.add(btn);
                }
                globalCol++;
            }

            // gap
            grid.add(new JPanel());

            // center block seats (always present)
            for (int c = 0; c < centerBlock; c++) {
                if (lastRowCenterOnly) {
                    // Center of last row: start at column 5 (globalCol will be 5 here)
                    String seatId = String.valueOf((char)('A' + r)) + (globalCol);
                    JToggleButton btn = createSeatButton(seatId, globalCol);
                    grid.add(btn);
                } else {
                    String seatId = String.valueOf((char)('A' + r)) + (globalCol);
                    JToggleButton btn = createSeatButton(seatId, globalCol);
                    grid.add(btn);
                }
                globalCol++;
            }

            // gap
            grid.add(new JPanel());

            // right block seats (or placeholders on last row)
            for (int c = 0; c < rightBlock; c++) {
                if (lastRowCenterOnly) {
                    JPanel placeholder = new JPanel();
                    placeholder.setOpaque(false);
                    grid.add(placeholder);
                } else {
                    String seatId = String.valueOf((char)('A' + r)) + (globalCol);
                    JToggleButton btn = createSeatButton(seatId, globalCol);
                    grid.add(btn);
                }
                globalCol++;
            }

            // right label
            JLabel rl = new JLabel(String.valueOf((char)('A' + r)), JLabel.CENTER);
            rl.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
            rl.setForeground(new Color(100,100,100));
            grid.add(rl);
        }

        center.add(grid, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(new EmptyBorder(8,12,8,12));
        counterLabel = new JLabel("선택 좌석 수: 0 / " + maxSelect);
        bottom.add(counterLabel, BorderLayout.WEST);
        JPanel actions = new JPanel();
        JButton ok = new JButton("확인");
        JButton cancel = new JButton("취소");
        actions.add(ok); actions.add(cancel);
        bottom.add(actions, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            if (selected.isEmpty()) { JOptionPane.showMessageDialog(this, "좌석을 선택해주세요."); return; }
            setVisible(false);
        });
        cancel.addActionListener(e -> { selected.clear(); setVisible(false); });

        pack();

        // scale overall window down to ~55% (slightly narrower) and slightly increase height for true squares
        int width = (int)(Math.min(900, 60 * cols) * 0.55);
        int height = (int)(Math.min(700, 40 * rows + 180) * 0.6);
        int extraVertical = 40; // slightly increase vertical room
        height += extraVertical;
        setSize(width, height);

        // enforce fixed size and compute square seat size so buttons don't stretch on resize
        setResizable(false);

        // compute seat sizing and set grid preferred size to enforce square cells
        int seatCols = leftBlock + centerBlock + rightBlock;
        int gapCount = 2; // two gaps between blocks
        int labelWidth = 34; // left/right labels reserved width
        int gapWidth = 8; // gap panel width
        int availGridWidth = Math.max(200, width - 80);
        int availGridHeight = Math.max(200, height - 140);
        int seatSize = Math.max(12, Math.min((availGridWidth - (2 * labelWidth) - (gapCount * gapWidth)) / seatCols,
            (availGridHeight) / rows));

        int gridWidth = seatCols * seatSize + (gapCount * gapWidth) + (2 * labelWidth);
        int gridHeight = rows * seatSize + (rows - 1) * cellGap;
        grid.setPreferredSize(new Dimension(gridWidth, gridHeight));

        // adjust components inside grid to use the computed seatSize
        for (Component comp : grid.getComponents()) {
            if (comp instanceof JToggleButton) {
                JToggleButton b = (JToggleButton) comp;
                b.setPreferredSize(new Dimension(seatSize, seatSize));
                // make seat numbers larger and bolder for stronger contrast
                b.setFont(new Font("Malgun Gothic", Font.BOLD, Math.max(12, seatSize * 3 / 4)));
            } else if (comp instanceof JPanel) {
                comp.setPreferredSize(new Dimension(gapWidth, seatSize));
            } else if (comp instanceof JLabel) {
                comp.setPreferredSize(new Dimension(labelWidth, seatSize));
            }
        }

        validate();
        setLocationRelativeTo(getOwner());
    }

    private JPanel makeBlockPanel(int blockCols) {
        JPanel pnl = new JPanel(new GridLayout(rows, blockCols, 6, 6));
        pnl.setAlignmentY(Component.TOP_ALIGNMENT);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < blockCols; c++) {
                JToggleButton btn = new JToggleButton(" ");
                // square-ish buttons
                btn.setPreferredSize(new Dimension(34, 34));
                btn.setFont(new Font("Malgun Gothic", Font.PLAIN, 11));
                btn.setMargin(new java.awt.Insets(0,0,0,0));
                pnl.add(btn);
            }
        }
        return pnl;
    }

    private void fillBlockLabels(JPanel pnl, int blockCols, int startCol) {
        Component[] comps = pnl.getComponents();
        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < blockCols; c++) {
                Component comp = comps[idx++];
                if (comp instanceof JToggleButton) {
                    JToggleButton btn = (JToggleButton) comp;
                    int globalCol = startCol + c;
                    btn.setText(String.valueOf(globalCol));
                    String seatId = String.valueOf((char)('A' + r)) + globalCol;
                    if (occupied.contains(seatId)) {
                        btn.setEnabled(false);
                        btn.setBackground(new Color(210,210,210));
                    } else {
                        btn.setBackground(new Color(220, 250, 220));
                        btn.addActionListener(new SeatActionListener(seatId, btn));
                    }
                }
            }
        }
    }


    private class SeatActionListener implements ActionListener {
        private final String seatId;
        private final JToggleButton btn;
        SeatActionListener(String seatId, JToggleButton btn) { this.seatId = seatId; this.btn = btn; }
        @Override public void actionPerformed(ActionEvent e) {
            Color available = Color.decode("#8BB665");
            Color unavailable = Color.decode("#A7A4A6");
            Color selectedBorder = Color.decode("#EFBB64");
            if (btn.isSelected()) {
                if (selected.size() >= maxSelect) {
                    btn.setSelected(false);
                    JOptionPane.showMessageDialog(SeatSelectionDialog.this, "선택 인원수를 초과했습니다.");
                    return;
                }
                selected.add(seatId);
                btn.setBackground(available);
                btn.setBorder(new LineBorder(selectedBorder, 2));
            } else {
                selected.remove(seatId);
                btn.setBackground(available);
                btn.setBorder(BorderFactory.createLineBorder(new Color(160,160,160)));
            }
            counterLabel.setText("선택 좌석 수: " + selected.size() + " / " + maxSelect);
        }
    }

    public List<String> getSelectedSeats() { return new ArrayList<>(selected); }

    private JToggleButton createSeatButton(String seatId, int colNumber) {
        JToggleButton btn = new JToggleButton(String.valueOf(colNumber));
        // scaled square button (~60%)
        int size = 20; // ~34 * 0.6
        btn.setPreferredSize(new Dimension(size, size));
        btn.setFont(new Font("Malgun Gothic", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setMargin(new java.awt.Insets(0,0,0,0));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        Color available = Color.decode("#8BB665");
        Color unavailable = Color.decode("#A7A4A6");
        if (occupied.contains(seatId)) {
            btn.setEnabled(false);
            btn.setBackground(unavailable);
            btn.setBorder(BorderFactory.createLineBorder(unavailable.darker()));
        } else {
            btn.setBackground(available);
            btn.setBorder(BorderFactory.createLineBorder(new Color(160,160,160)));
            btn.addActionListener(new SeatActionListener(seatId, btn));
        }
        return btn;
    }
}
