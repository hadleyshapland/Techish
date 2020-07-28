package com.google.sps.algorithms;

import com.google.sps.data.MatchRepository;
import com.google.sps.data.MatchRequest;
import com.google.sps.data.PersistentUserRepository;
import com.google.sps.data.User;
import com.google.sps.data.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class MatchQuery {
  
  private PersistentUserRepository userRepository = PersistentUserRepository.getInstance();

  /**
  * This method takes a MatchRequest and a Collection of users that are already saved and returns 
  * all Users in the User Repository that match the criteria in MatchRequest AND are not already
  * saved in the userSavedMatches collection.
  */
  public Collection<User> query(MatchRequest request, Collection<User> userSavedMatches) {
    return query(request, userSavedMatches, userRepository);
  }

  //overload of query allows UserRepository to be passed in for testing
  public Collection<User> query(MatchRequest request, Collection<User> userSavedMatches, UserRepository userRepository) {
      
    Collection<User> allUsers = userRepository.getAllUsers();
    Collection<User> mentorMatches = new ArrayList<User>();

    String matchCriteria = request.getCriteria();

    //return empty collection if there is no criteria
    if(matchCriteria == "") {
      return mentorMatches;
    }

    for(User potentialMentor : allUsers) {
        Collection<String> mentorSpecialties = potentialMentor.getSpecialties();

        //see if new mentor is not already saved AND contains correct criteria
        if(!(userSavedMatches.contains(potentialMentor)) && (mentorSpecialties.contains(matchCriteria))) {
            mentorMatches.add(potentialMentor);
        }
    }

    List<User> rankedMatches = MatchRanking.rankMatches(userSavedMatches, allUsers, mentorMatches);
    
    return rankedMatches;
  }

}
