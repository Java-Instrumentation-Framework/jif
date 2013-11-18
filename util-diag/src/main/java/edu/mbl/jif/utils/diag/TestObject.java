package edu.mbl.jif.utils.diag;

import java.io.Serializable;

/**
 * Object for Testing Object Testing Tester
 */

public class TestObject
    implements Serializable {

  // static fields are not stored to XML

   String s = "scoobie-doo";
   String stringWithNewLines = "This string is several lines long.\n"+
      "lajkLJHADSF; LJAS; LKDFJLAKSDFAS \n" +
      "ASDFASDFASDFASDFASDF ASDFASFDASDF\n" +
      "ASDFA ADSF ASDF ASDFASDF ASFADS FADS FASFD \n" +
      "ADS FASD FASDF AF ASDF AS\n" ;

  static int x = 2;

  public TestObject() {
  }


  public String getS() {
    return s;
  }

  public void setS(String _s) {
    s = _s;
  }

  public int getX() {
    return x;
  }

  public void setX(int _x) {
    x = _x;
  }
  public String toString() {
    return(s + ", " + x);
  }

}
