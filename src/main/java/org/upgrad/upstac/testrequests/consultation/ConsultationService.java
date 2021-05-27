package org.upgrad.upstac.testrequests.consultation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.upgrad.upstac.exception.AppException;
import org.upgrad.upstac.testrequests.TestRequest;
import org.upgrad.upstac.testrequests.lab.LabResult;
import org.upgrad.upstac.users.User;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    private static Logger logger = LoggerFactory.getLogger(ConsultationService.class);


    @Transactional
    public Consultation assignForConsultation( TestRequest testRequest, User doctor) {
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setRequest(testRequest);

        return    consultationRepository.save(consultation);


    }

    public Consultation updateConsultation(TestRequest testRequest , CreateConsultationRequest createConsultationRequest) {
        Consultation consultation = consultationRepository.findByRequest(testRequest).orElseThrow(()-> new AppException("Invalid Request"));

        consultation.setSuggestion(createConsultationRequest.getSuggestion());
        consultation.setComments(createConsultationRequest.getComments());
        consultation.setUpdatedOn(LocalDate.now());

        return consultationRepository.save(consultation);


    }

    public List<TestRequest> getAssignedConsultations(User doctor){
        //getting consultations that doctor has worked upon
        List<Consultation> consultations=consultationRepository.findByDoctor(doctor);
        //extracting test requests made out of consultations
        List<TestRequest> testRequests= new ArrayList<>();
        for(Consultation consultation:consultations){
            testRequests.add(consultation.getRequest());
        }
        return testRequests;
    }


}
