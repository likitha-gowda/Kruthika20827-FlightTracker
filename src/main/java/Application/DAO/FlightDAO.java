package Application.DAO;

import Application.Model.Flight;
import Application.Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    // ✅ Get all flights
    public List<Flight> getAllFlights(){
        Connection connection = ConnectionUtil.getConnection();
        List<Flight> flights = new ArrayList<>();
        try {
            String sql = "SELECT * FROM flight";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("departure_city"),
                        rs.getString("arrival_city")
                );
                flights.add(flight);
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return flights;
    }

    // ✅ Get flight by ID
    public Flight getFlightById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM flight WHERE flight_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("departure_city"),
                        rs.getString("arrival_city")
                );
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // ✅ Insert new flight
    public Flight insertFlight(Flight flight){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO flight (departure_city, arrival_city) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, flight.getDeparture_city());
            preparedStatement.setString(2, flight.getArrival_city());

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generatedId = pkeyResultSet.getInt(1);
                return new Flight(generatedId, flight.getDeparture_city(), flight.getArrival_city());
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // ✅ Update flight
    public void updateFlight(int id, Flight flight){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE flight SET departure_city = ?, arrival_city = ? WHERE flight_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, flight.getDeparture_city());
            preparedStatement.setString(2, flight.getArrival_city());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    // ✅ Get flights by route
    public List<Flight> getAllFlightsFromCityToCity(String departure_city, String arrival_city){
        Connection connection = ConnectionUtil.getConnection();
        List<Flight> flights = new ArrayList<>();

        try {
            String sql = "SELECT * FROM flight WHERE departure_city = ? AND arrival_city = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, departure_city);
            preparedStatement.setString(2, arrival_city);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("departure_city"),
                        rs.getString("arrival_city")
                );
                flights.add(flight);
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return flights;
    }
}