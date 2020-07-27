package com.google.sps.algorithms;

import com.google.sps.data.MatchRepository;
import com.google.sps.data.MatchRequest;
import com.google.sps.data.PersistentUserRepository;
import com.google.sps.data.User;
import com.google.sps.data.UserRepository;
import java.lang.Exception;
import java.io.IOException; 
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


public final class AbuseDetection {

    // private Collection <LocalTime> timesOfRequests = new ArrayList<LocalTime>();
    private Collection <Date> timesOfRequests = new ArrayList<Date>();

    private int requestCounter = 0;
    private int requestsDropped = 0;

    private final int currentNumRequestsAllowed;
    private final long timePeriod;
  
  /**
  * Constructor that initializes currentNumRequestsAllowed and timePeriod
  */
  public AbuseDetection(long timeValue, int currentCounter) { 
    this.timePeriod = timeValue;
    this.currentNumRequestsAllowed = currentCounter;
  }

  /**
  *  Function takes a local time, tries to add requests only if there are not
  *  more than the currentRequestsAllowed variable, and less than the 
  *  timePeriod. If added the method returns true, if not it returns false.
  */
   public boolean addRequest(LocalTime currentTime) {
        Instant currentTimeToInstance =  currentTime.atDate(LocalDate.now()).
        atZone(ZoneId.systemDefault()).toInstant();
        Date timeToDate = Date.from(currentTimeToInstance);
    
        boolean returnValue = false;
        if(requestCounter < currentNumRequestsAllowed) {
            timesOfRequests.add(timeToDate);

            returnValue = true;
            requestCounter++;
            System.out.println("Current Num: " + requestCounter);

        }
        else {
            System.out.println("Current Num: " + requestCounter);
            for(Date currentDate : timesOfRequests) {
                Instant instant = Instant.ofEpochMilli(currentDate.getTime());
                LocalTime currentDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();

                Duration timeDifference = Duration.between(currentDateTime, currentTime);
                long difference = timeDifference.getSeconds();

                if(difference >= timePeriod) {
                    timesOfRequests.remove(currentDate);
                    requestsDropped++;
                  
                    timesOfRequests.add(timeToDate);
                    returnValue = true;
                    break;
                }
            }
        }

        return returnValue;
        // keep track of dropped requests
        // converting local time to date
        // deleting requests after certain amount of seconds
         
    
        
  }

}