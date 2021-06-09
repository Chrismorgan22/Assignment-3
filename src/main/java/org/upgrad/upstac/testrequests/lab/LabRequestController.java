package org.upgrad.upstac.testrequests.lab;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.upgrad.upstac.config.security.UserLoggedInService;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.RequestStatus;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.TestRequestQueryService;
import org.upgrad.upstac.testrequests.TestRequestUpdateService;
import org.upgrad.upstac.testrequests.flow.TestRequestFlowService;
import org.upgrad.upstac.users.User;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.upgrad.upstac.exception.UpgradResponseStatusException.asBadRequest;
import static org.upgrad.upstac.exception.UpgradResponseStatusException.asConstraintViolation;


@RestController
@RequestMapping("/api/labrequests")
public class LabRequestController {

    Logger log = LoggerFactory.getLogger(LabRequestController.class);




    @Autowired
    private TestRequestUpdateService testRequestUpdateService;

    @Autowired
    private TestRequestQueryService testRequestQueryService;

    @Autowired
    private TestRequestFlowService testRequestFlowService;



    @Autowired
    private UserLoggedInService userLoggedInService;



    @GetMapping("/to-be-tested")
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTests()  {

// explain this method
        //Implement this method to return the list of test remarks having status as 'INITIATED'
        //Make use of the findBy() method from testRequestQueryService class
        //For reference check the method requestHistory() method from TestRequestController class

       return testRequestQueryService.findBy(RequestStatus.INITIATED);




    }

    @GetMapping
    @PreAuthorize("hasAnyRole('TESTER')")
    public List<TestRequest> getForTester()  {

        // Implement This Method

        // Create an object of User class and store the current logged in user first
        //Implement this method to return the list of test requests assigned to current tester(make use of the above created User object)
        //Make use of the findByTester() method from testRequestQueryService class
        // For reference check the method getForTests() method from LabRequestController class
        return testRequestQueryService.findBy(RequestStatus.INITIATED);




    }


    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/assign/{id}")
    public TestRequest assignForLabTest(@PathVariable Long id) {
//exaplain this method
        //Implement this method to assign a particular test request to the current tester(logged in user)
        // Create an object of User class and get the current logged in user
        //Create an object of TestRequest class and use the assignForLabTest() method of testRequestUpdateService to assign particular id
        //return the above created object
        //refer to the method createRequest() from the TestRequestController class


        User tester =userLoggedInService.getLoggedInUser();

      return   testRequestUpdateService.assignForLabTest(id,tester);
    }

    @PreAuthorize("hasAnyRole('TESTER')")
    @PutMapping("/update/{id}")
    public TestRequest updateLabTest(@PathVariable Long id,@RequestBody CreateLabResult createLabResult) {
//explain this code
        //Implement this method to update the result of the current test request id with test result
        //Create an object of the user class to get logged in user
        //Create an object of testresult class and make use of updateLabTest() method from testRequestUpdateService class
        //to update the current test request id with the createLabResult details by the current user(object created)

        try {

            User tester=userLoggedInService.getLoggedInUser();
            return testRequestUpdateService.updateLabTest(id,createLabResult,tester);


        } catch (ConstraintViolationException e) {
            throw asConstraintViolation(e);
        }catch (AppException e) {
            throw asBadRequest(e.getMessage());
        }
    }





}
