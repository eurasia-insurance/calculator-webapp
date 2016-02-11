package kz.theeurasia.policy.osgpovts.view;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import kz.theeurasia.policy.GlobalMessageBundleCode;
import kz.theeurasia.policy.osgpovts.ValidationException;
import kz.theeurasia.policy.osgpovts.domain.InsuredDriver;
import kz.theeurasia.policy.osgpovts.domain.InsuredVehicle;
import kz.theeurasia.policy.osgpovts.domain.PolicyRequest;
import kz.theeurasia.policy.osgpovts.facade.DriverFacade;
import kz.theeurasia.policy.osgpovts.facade.PolicyFacade;
import kz.theeurasia.policy.osgpovts.facade.VehicleFacade;

@ManagedBean(name = "osgpovtsView")
@ViewScoped
public class OSGPOVTSView implements Serializable {

    private static final long serialVersionUID = 6605204552704504011L;

    @ManagedProperty("#{glb}")
    private ResourceBundle glb;

    @ManagedProperty("#{gpovts}")
    private ResourceBundle gpovts;

    @ManagedProperty("#{policyFacade}")
    private PolicyFacade policyFacade;

    @ManagedProperty("#{driverFacade}")
    private DriverFacade driverFacade;

    @ManagedProperty("#{vehicleFacade}")
    private VehicleFacade vehicleFacade;

    private PolicyRequest policy;

    @PostConstruct
    public void init() {
	try {
	    this.policy = policyFacade.initNew();
	    driverFacade.add(policy);
	    vehicleFacade.add(policy);
	} catch (ValidationException e) {
	    FacesContext
		    .getCurrentInstance()
		    .addMessage(null,
			    new FacesMessage(FacesMessage.SEVERITY_FATAL,
				    glb.getString(GlobalMessageBundleCode.MESSAGES_SERVER_FATAL_ERROR_CAPTION
					    .getMessageBundleCode()),
				    glb.getString(GlobalMessageBundleCode.MESSAGES_SERVER_FATAL_ERROR_DESCRIPTION
					    .getMessageBundleCode())));
	}
    }

    public void addInsuredDriver() {
	try {
	    driverFacade.add(policy);
	} catch (ValidationException e) {
	    FacesContext
		    .getCurrentInstance()
		    .addMessage(null,
			    new FacesMessage(FacesMessage.SEVERITY_WARN,
				    gpovts.getString(e.getMessageCode().getMessageBundleCode()),
				    gpovts.getString(e.getDescriptionCode().getMessageBundleCode())));
	}
	policyFacade.calculatePremiumCost(policy);
    }

    public void removeInsuredDriver(InsuredDriver driver) {
	try {
	    driverFacade.remove(policy, driver);
	} catch (ValidationException e) {
	    FacesContext
		    .getCurrentInstance()
		    .addMessage(null,
			    new FacesMessage(FacesMessage.SEVERITY_WARN,
				    gpovts.getString(e.getMessageCode().getMessageBundleCode()),
				    gpovts.getString(e.getDescriptionCode().getMessageBundleCode())));
	}
	policyFacade.calculatePremiumCost(policy);
    }

    public void addInsuredVehicle() {
	try {
	    vehicleFacade.add(policy);
	} catch (ValidationException e) {
	    FacesContext
		    .getCurrentInstance()
		    .addMessage(null,
			    new FacesMessage(FacesMessage.SEVERITY_WARN,
				    gpovts.getString(e.getMessageCode().getMessageBundleCode()),
				    gpovts.getString(e.getDescriptionCode().getMessageBundleCode())));
	}
	policyFacade.calculatePremiumCost(policy);
    }

    public void removeInsuredVehicle(InsuredVehicle insuredVehicle) {
	try {
	    vehicleFacade.remove(policy, insuredVehicle);
	} catch (ValidationException e) {
	    FacesContext
		    .getCurrentInstance()
		    .addMessage(null,
			    new FacesMessage(FacesMessage.SEVERITY_WARN,
				    gpovts.getString(e.getMessageCode().getMessageBundleCode()),
				    gpovts.getString(e.getDescriptionCode().getMessageBundleCode())));
	}
	policyFacade.calculatePremiumCost(policy);
    }

    public void doCalculatePolicyCost() {
	policyFacade.calculatePremiumCost(policy);
    }

    public void onDriverIdNumberChanged(InsuredDriver insuredDriver) {
	try {
	    driverFacade.fetchInfo(policy, insuredDriver);
	} catch (ValidationException e) {
	}
	policyFacade.calculatePremiumCost(policy);
    }

    public void onFormChanged() {
	policyFacade.calculatePremiumCost(policy);
    }

    public void onVehicleVinCodeChanged(InsuredVehicle insuredVehicle) {
	try {
	    vehicleFacade.fetchInfo(policy, insuredVehicle);
	} catch (ValidationException e) {
	}
	policyFacade.calculatePremiumCost(policy);
    }

    public void onVehicleRegionChanged(InsuredVehicle insuredVehicle) {
	vehicleFacade.evaluateMajorCity(insuredVehicle);
	policyFacade.calculatePremiumCost(policy);
    }

    // GENERATED

    public void setGlb(ResourceBundle glb) {
	this.glb = glb;
    }

    public void setPolicyFacade(PolicyFacade policyFacade) {
	this.policyFacade = policyFacade;
    }

    public void setDriverFacade(DriverFacade driverFacade) {
	this.driverFacade = driverFacade;
    }

    public void setVehicleFacade(VehicleFacade vehicleFacade) {
	this.vehicleFacade = vehicleFacade;
    }

    public void setGpovts(ResourceBundle gpovts) {
	this.gpovts = gpovts;
    }

    public PolicyRequest getPolicy() {
	return policy;
    }
}