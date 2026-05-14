import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.Scanner;

public class HotelReservationSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "abcd1234";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            while(true) {
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner scanner = new Scanner(System.in);
                System.out.println("1. Reserve a Room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.println("Choose an Option : ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection,scanner);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner);
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        break;
                    case 5:
                        deleteReservation(connection,scanner);
                        break;
                    case 0:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid Choice . Try Again");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw  new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection connection,Scanner scanner) {
        try {
            System.out.println("Enter Guest Name : ");
            String guestName = scanner.next();
            scanner.nextLine();
            System.out.println("Enter room number : ");
            int roomNumber = scanner.nextInt();
            System.out.println("Enter contact No : ");
            String contact = scanner.next();
            String sql = "INSERT INTO reservations(guest_name,room_no,phone_no) VALUES(?,?,?)";
            try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1,guestName);
                pstmt.setInt(2,roomNumber);
                pstmt.setString(3,contact);
                int affectedRows = pstmt.executeUpdate();

                if(affectedRows > 0) {
                    System.out.println("Reservation Successfull");
                } else {
                    System.out.println("Reservation Failed");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void viewReservations(Connection connection) {
        String sql = "SELECT * FROM reservations";
        try(Statement st = connection.createStatement()) {
            ResultSet roomData = st.executeQuery(sql);

            System.out.println("CURRENT RESERVATION");
            System.out.println();

            while(roomData.next()) {
                int rid = roomData.getInt(1);
                String guestName = roomData.getString(2);
                int roomNum = roomData.getInt(3);
                String contactNum = roomData.getString(4);
                String rdate = roomData.getTimestamp(5).toString();
                System.out.println("---------------------------");
                System.out.println("ID " + rid);
                System.out.println("Name : "+ guestName);
                System.out.println("Room Num : "+roomNum);
                System.out.println("Contact Num : "+contactNum);
                System.out.println("Reservation Date : "+rdate);
                System.out.println("---------------------------");
            }
            System.out.println();

        } catch (SQLException e) {
            System.out.println("SQL Exception Occurred" + e.getMessage());
        }
    }

    private static void getRoomNumber(Connection connection,Scanner scanner) {
        try {
            System.out.println("Enter Reservation Id : ");
            int reservationId = scanner.nextInt();
            System.out.println("Enter Guest Name : ");
            String guestName = scanner.next();

            String sql = "SELECT room_no FROM reservations WHERE reserve_id = "+ reservationId +" AND guest_name='" + guestName+"';";

            try(Statement st = connection.createStatement()) {
                ResultSet rs = st.executeQuery(sql);

                if(rs.next()) {
                    int rno = rs.getInt("room_no");
                    System.out.println("Rooom Number for Reservation ID " + reservationId + " and Guest " + guestName + " is : " + rno);
                } else {
                    System.out.println("Reservation not found for the given ID and guest Name");
                }

            } catch (SQLException e) {
                System.out.println(e);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void updateReservation(Connection connection,Scanner scanner) {
        try {
            System.out.println("Enter Reservation ID to Update : ");
            int rid = scanner.nextInt();
            scanner.nextLine();

            if(!reservationExists(connection,rid)) {
                System.out.println("reservation not Found for the given ID.");
                return;
            }

            System.out.println("Enter new Guest Name : ");
            String newGuestName = scanner.nextLine();
            System.out.println("Enter new room Number : ");
            int rno = scanner.nextInt();
            System.out.println("Enter new Contact no : ");
            String cno = scanner.next();

            String sql = "UPDATE reservations SET guest_name= '"+newGuestName+"',room_no="+rno+",phone_no="+"'"+cno+"'"+" WHERE reserve_id="+rid;

            try(Statement st = connection.createStatement()) {
                int affectedRows = st.executeUpdate(sql);

                if(affectedRows > 0) {
                    System.out.println("Reservation updated Successfully");
                } else {
                    System.out.println("reservation update Failed");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection connection,Scanner scanner) {
        try {
            System.out.println("Enter reservation Id to delete : ");
            int rid = scanner.nextInt();

            if(!reservationExists(connection,rid)) {
                System.out.println("reservation Not Found for given Id");
                return;
            }

            String sql = "DELETE FROM reservations WHERE reserve_id = "+rid;

            try(Statement st = connection.createStatement()) {
                int affectedRows = st.executeUpdate(sql);

                if(affectedRows > 0) {
                    System.out.println("Reservation Deleted Successfully");
                } else {
                    System.out.println("Reservation deleted Failed");
                }

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection connection,int rid) {
        try {
            String sql = "SELECT reserve_id FROM reservations WHERE reserve_id="+rid;

            try(Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(sql);
                return rs.next();
            } catch (SQLException e) {
             System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void exit() throws InterruptedException {
        System.out.println("Exiting System");
        int i=5;
        while(i!=0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println("\nThank you For Visiting Us.");
    }

}
