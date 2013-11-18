/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.imaging.nav.util;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

/**
 *
 * @author GBH
 */
public class ComboBoxWithToolTipRenderer extends BasicComboBoxRenderer {

      String[] tooltip;

      public ComboBoxWithToolTipRenderer(String[] tooltip) {
         this.tooltip = tooltip;
      }

      @Override
      public Component getListCellRendererComponent(JList list, Object value,
              int index, boolean isSelected, boolean cellHasFocus) {
         if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            if (-1 < index) {
               list.setToolTipText(tooltip[index]);
            }
         } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
         }
         setFont(list.getFont());
         setText((value == null) ? "" : value.toString());
         return this;
      }
   }