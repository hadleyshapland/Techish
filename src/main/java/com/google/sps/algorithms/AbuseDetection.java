package com.google.sps.algorithms;

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

    private Collection <Date> timesOfRequests = new ArrayList<Date>();

    private int requestCounter = 0;
    private int requestsDropped = 0;

    private final int currentNumRequestsAllowed;
    private final Duration timePeriod;
  
  /**
  * Constructor that initializes currentNumRequestsAllowed and timePeriod
  * The requestInterval represents the amount of seconds passed in for
  * this time interval of requests.
  * The requestsAllowed is the amount of requests that are being allowed
  * during that timeValue interval.
  */
  public AbuseDetection(Duration requestInterval, int requestsAllowed) { 
    this.timePeriod = requestInterval;
    this.currentNumRequestsAllowed = requestsAllowed;
  }

  /**
  *  Function takes a local time, tries to add requests only if there are not
  *  more than the currentRequestsAllowed variable, and less than the 
  *  timePeriod. If added the method returns true, if not it returns false.
  */
   public boolean addRequest(LocalTime currentTime) {
        Instant currentTimeToInstance =  currentTime.atDate(LocalDate.now()).
        atZone(ZoneId.systemDefault()).toInstant();
        Date datetime = Date.from(currentTimeToInstance);
    
        if(requestCounter < currentNumRequestsAllowed) {
            timesOfRequests.add(datetime);
            requestCounter++;
            return true;
        }
        else {

            for(Date requestTime : timesOfRequests) {
                Instant instant = Instant.ofEpochMilli(requestTime.getTime());
                LocalTime requestIteratedInLocalTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();

                Duration timeDifference = Duration.between(requestIteratedInLocalTime, currentTime);
                long sec = timeDifference.getSeconds();
                int seconds = (int) sec;

                if(timeDifference.compareTo(timePeriod) > 0 || seconds < timeDifference.compareTo(timePeriod)) {
                    timesOfRequests.remove(requestTime);
                    requestsDropped++;

                    timesOfRequests.add(datetime);
                    return true;
                }
            }
        }

        return false;
  }

}