/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.mbl.jif.imaging.nav.util;

import edu.mbl.jif.imaging.nav.util.FavoritePathsComboBox.FavoritePathsComboBoxListener;

/**
 *
 * @author GBH
 */
public class TestFavoritePathsComboBoxListener implements FavoritePathsComboBoxListener {

   public void favoritePathSelected(String path) {
      System.out.println("Favorite Selected: " + path);
   }
 
   public static void main(String[] args) {
      
   }
}
