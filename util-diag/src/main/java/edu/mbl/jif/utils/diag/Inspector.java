/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.mbl.jif.utils.diag;

import javax.swing.JPanel;

/**
 *
 * @author GBH
 */
public class Inspector {

    Object[] inspectables;

    public Inspector(Object[] inspectables)
      {
        this.inspectables = inspectables;
        for (int i = 0; i < inspectables.length; i++) {
            Object object = inspectables[i];
            //org.pf.joi.Inspector.inspect(object);
        }
      }

    public static void main(String[] args)
      {
        new Inspector(new Object[] {new JPanel()});
      }
}
