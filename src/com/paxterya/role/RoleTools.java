package com.paxterya.role;

public class RoleTools {
  private RoleTools(){}

  public static boolean isRoleIdValid(int roleID){
    if(roleID >= 0 && roleID <= 9){
      return true;
    }else{
      return false;
    }
  }
}
