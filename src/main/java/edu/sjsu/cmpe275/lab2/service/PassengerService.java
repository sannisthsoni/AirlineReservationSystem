package edu.sjsu.cmpe275.lab2.service;

import edu.sjsu.cmpe275.lab2.entity.Flight;
import edu.sjsu.cmpe275.lab2.entity.Passenger;
import edu.sjsu.cmpe275.lab2.entity.Reservation;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PassengerService {
    @Autowired
    PassengerRepository passengerRepository;

    public PassengerService(){
        super();
    }

    public PassengerService(PassengerRepository passengerRepository){
        this.passengerRepository =  passengerRepository;
    }

    public Passenger save(Passenger passenger){
        return passengerRepository.save(passenger);
    }

    public ResponseEntity findByPassengerId(String id){
        Passenger pObj= null;
        HttpStatus status = null;
        Passenger passenger = null;
        JSONObject jsonObject = new JSONObject();
        try{
            passenger = passengerRepository.findByPassengerId(id);
            if(passenger!=null){
                status = HttpStatus.OK;
                jsonObject = passenger.getWholePassengerDetailsJSON();
            }
            else {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("msg", "Failed to delete Passenger with ID "+ id);
                jsonObject1.put("code", status);
                jsonObject.put("Bad Request",jsonObject1);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity(jsonObject.toString(),status);
//        return new ResponseEntity(passenger, status);
    }

    public List<Passenger> findAllPassengers(){
        return passengerRepository.findAll();
    }

    public Passenger updatePassenger(String id, Map<String,String> map) {
        Passenger passenger = passengerRepository.findByPassengerId(id);
        passenger.setFirstname(map.get("firstname"));
        passenger.setLastname(map.get("lastname"));
        passenger.setAge(Integer.parseInt(map.get("age")));
        passenger.setGender(map.get("gender"));
        passenger.setPhone(map.get("phone"));

        return passenger;

    }

    public Passenger createPassenger(Map<String,String> map){
        Passenger passenger = new Passenger();
        passenger.setFirstname(map.get("firstname"));
        passenger.setLastname(map.get("lastname"));
        passenger.setGender(map.get("gender"));
        passenger.setAge(Integer.parseInt(map.get("age")));
        passenger.setPhone(map.get("phone"));

        return save(passenger);
    }

    public ResponseEntity deletePassenger(String id){
        //passengerRepository.deletePassengerByPassengerId(id);
        String msg="";
        Passenger passenger = passengerRepository.findByPassengerId(id);
        if(passenger==null){
            //msg = "Passenger "+id+" not found";
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("msg", "Failed to delete Passenger with ID "+ id);
            jsonObject1.put("code", HttpStatus.NOT_FOUND);
            jsonObject.put("Bad Request",jsonObject1);
            return new ResponseEntity(jsonObject,HttpStatus.NOT_FOUND);
        }
        else{
            List<Reservation> reservations = passenger.getReservations();
            for(Reservation reservation : reservations){
                for(Flight flight : reservation.getFlights()){
                    flight.setSeatsLeft(flight.getSeatsLeft()+1);
                }
            }
            passengerRepository.deletePassengerByPassengerId(id);
            return new ResponseEntity(msg,HttpStatus.OK);
        }

    }


}
