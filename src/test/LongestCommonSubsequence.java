package com.api.test;
public class LongestCommonSubsequence
{
 
  String lcs( char[] X, char[] Y, int m, int n )
  {
    if (m == 0 || n == 0)
      return "";
    if (X[m-1] == Y[n-1])
      return  lcs(X, Y, m-1, n-1) +X[m-1];
    else
      return max(lcs(X, Y, m, n-1), lcs(X, Y, m-1, n));
  }
 
  String max(String a, String b)
  {
    return (a.length() >= b.length())? a : b;
  }
 
  public static void main(String[] args)
  {
    LongestCommonSubsequence lcs = new LongestCommonSubsequence();
    String s1 = "AGGTAB";
    String s2 = "GXTXbvcgdAYB";
 
    char[] X=s1.toCharArray();
    char[] Y=s2.toCharArray();
    int m = X.length;
    int n = Y.length;
 
    System.out.println("Length of LCS is" + " " +
                                  lcs.lcs( X, Y, m, n ) );
  }
 
}
 
