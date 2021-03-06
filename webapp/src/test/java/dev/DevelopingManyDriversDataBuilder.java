package dev;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.ProjectStage;
import javax.inject.Inject;

import com.lapsa.insurance.domain.policy.Policy;
import com.lapsa.insurance.domain.policy.PolicyDriver;
import com.lapsa.insurance.domain.policy.PolicyVehicle;
import com.lapsa.insurance.elements.IdentityCardType;
import com.lapsa.insurance.elements.InsuredAgeClass;
import com.lapsa.insurance.elements.InsuredExpirienceClass;
import com.lapsa.insurance.elements.Sex;
import com.lapsa.international.country.Country;
import com.lapsa.kz.country.KZArea;
import com.lapsa.kz.country.KZCity;

import kz.theeurasia.policy.calc.api.CalculationFacade;
import kz.theeurasia.policy.calc.api.DefaultCalculationDataBuilder;
import kz.theeurasia.policy.calc.api.DriverFacade;
import kz.theeurasia.policy.calc.api.ValidationException;
import kz.theeurasia.policy.calc.api.VehicleFacade;
import kz.theeurasia.policy.calc.bean.dataBuilder.ProjectStageDepend;
import tech.lapsa.kz.taxpayer.TaxpayerNumber;

@RequestScoped
@ProjectStageDepend(stage = ProjectStage.Development)
@ManyDrivers
public class DevelopingManyDriversDataBuilder implements DefaultCalculationDataBuilder {

    @Inject
    private DriverFacade driverFacade;

    @Inject
    private VehicleFacade vehicleFacade;

    @Inject
    private CalculationFacade calculationFacade;

    @Inject
    private Logger logger;

    @Override
    public void buildDefaultData(Policy calculation) {
	try {
	    PolicyDriver drv1 = driverFacade.add(calculation);
	    drv1.setIdNumber(TaxpayerNumber.of("570325300699"));
	    driverFacade.fetchInfo(calculation, drv1);
	    drv1.setExpirienceClass(InsuredExpirienceClass.MORE2);
	    drv1.getResidenceData().setCity(KZCity.ALM);
	    drv1.getResidenceData().setAddress("Джамбула, 231");
	    drv1.getResidenceData().setResident(true);
	    drv1.getOriginData().setCountry(Country.KAZ);
	    drv1.getDriverLicenseData().setNumber("123");
	    drv1.getDriverLicenseData().setDateOfIssue(LocalDate.now());
	    drv1.setHasAnyPrivilege(true);

	    PolicyDriver drv2 = driverFacade.add(calculation);
	    drv2.setIdNumber(TaxpayerNumber.of("870622300359"));
	    driverFacade.fetchInfo(calculation, drv2);
	    drv2.setExpirienceClass(InsuredExpirienceClass.MORE2);
	    drv2.getResidenceData().setCity(KZCity.ALM);
	    // drv2.getResidenceData().setAddress("Джамбула, 231");
	    // drv2.getResidenceData().setResident(true);
	    // drv2.getOriginData().setCountry(CountryDict.KAZ);
	    drv2.getDriverLicenseData().setNumber("123");
	    drv2.getDriverLicenseData().setDateOfIssue(LocalDate.now());
	    drv2.setHasAnyPrivilege(false);

	    PolicyDriver drv3 = driverFacade.add(calculation);
	    drv3.setIdNumber(TaxpayerNumber.of("800225000319"));
	    driverFacade.fetchInfo(calculation, drv3);
	    drv3.setAgeClass(InsuredAgeClass.OVER25);
	    drv3.setExpirienceClass(InsuredExpirienceClass.MORE2);
	    drv3.getPersonalData().setName("Вадим");
	    drv3.getPersonalData().setSurename("Исаев");
	    drv3.getPersonalData().setPatronymic("Олегович");
	    drv3.getPersonalData().setDateOfBirth(LocalDate.of(1980, 2, 25));
	    drv3.getPersonalData().setGender(Sex.MALE);
	    drv3.getIdentityCardData().setType(IdentityCardType.PASSPORT);
	    drv3.getIdentityCardData().setDateOfIssue(LocalDate.now());
	    drv3.getIdentityCardData().setIssuingAuthority("МВД РФ");
	    drv3.getIdentityCardData().setNumber("123123123");
	    drv3.getResidenceData().setCity(KZCity.ALM);
	    drv3.getResidenceData().setAddress("Джамбула, 231");
	    drv3.getResidenceData().setResident(true);
	    drv3.getOriginData().setCountry(Country.KAZ);
	    drv3.getDriverLicenseData().setNumber("123");
	    drv3.getDriverLicenseData().setDateOfIssue(LocalDate.now());
	    drv3.setHasAnyPrivilege(false);

	    PolicyDriver drv4 = driverFacade.add(calculation);
	    drv4.setIdNumber(TaxpayerNumber.of("860401402685"));
	    driverFacade.fetchInfo(calculation, drv4);
	    drv4.setExpirienceClass(InsuredExpirienceClass.MORE2);
	    drv4.getPersonalData().setGender(Sex.FEMALE);
	    drv4.getIdentityCardData().setIssuingAuthority("МВД РК");

	    drv4.getResidenceData().setCity(KZCity.ALM);
	    drv4.getResidenceData().setAddress("Джамбула, 231");
	    drv4.getResidenceData().setResident(true);
	    drv4.getOriginData().setCountry(Country.KAZ);
	    drv4.getDriverLicenseData().setNumber("123");
	    drv4.getDriverLicenseData().setDateOfIssue(LocalDate.now());
	    drv4.setHasAnyPrivilege(false);

	    PolicyVehicle vhc1 = vehicleFacade.add(calculation);
	    vhc1.setVinCode("JN1TANS51U0303376");
	    vhc1.setArea(KZArea.GALM);
	    vehicleFacade.evaluateMajorCity(vhc1);

	    calculationFacade.calculatePremiumCost(calculation);
	} catch (ValidationException e) {
	    logger.log(Level.SEVERE, e.getMessage(), e);
	}
    }

}
