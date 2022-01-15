package com.kay.monitor00;

/***
 * package com.kay;
 *
 * public class UserService {
 *     public UserService() {
 *     }
 *
 *     public String getUserInfo(String id) {
 *         long var2 = System.nanoTime(); // generate
 *
 *         System.out.println("get user info");
 *         System.out.println("get user info");
 *         System.out.println("get user info");
 *
 *         System.out.println("Method [getUserInfo] time cost:" + (System.nanoTime() - var2) + "(ns)"); // generate
 *         return id;
 *     }
 * }
 */
public class UserService {

    //method to be monitored
    public String getUserInfo(String id) {
        System.out.println("get user info");
        System.out.println("get user info");
        System.out.println("get user info");
//        throw new RuntimeException("eeeeexxx");
        return id;
    }
}
