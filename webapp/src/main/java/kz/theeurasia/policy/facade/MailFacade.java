package kz.theeurasia.policy.facade;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.lapsa.mailutil.InvalidMessageException;
import com.lapsa.mailutil.MailException;
import com.lapsa.mailutil.MailMessage;
import com.lapsa.mailutil.MailMessageBuilder;
import com.lapsa.mailutil.MailMessagePart;
import com.lapsa.mailutil.MailSender;
import com.lapsa.mailutil.MailService;

import kz.theeurasia.policy.domain.InsuredDriverData;
import kz.theeurasia.policy.domain.InsuredVehicleData;
import kz.theeurasia.policy.domain.PolicyRequestData;
import kz.theeurasia.policy.domain.UploadedImage;

@ManagedBean
@ViewScoped
public class MailFacade {

    private static final String TEMPLATE_NAME = "/emailTemplates/PolicyReuest.html";
    private static final String DEFAULT_SUBJECT = "Получена новая заявка на полис ОС ГПО ВТС";
    private static final String DEFAULT_TO_RECIPIENTS = "vadim.isaev@theeurasia.kz";

    @EJB
    private MailService mailHelper;

    private VelocityEngine ve;

    @PostConstruct
    public void initTemplate() {
	ve = new VelocityEngine();
	ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
	ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
	ve.setProperty("input.encoding", "UTF-8");
	ve.init();
    }

    public void sendNotice(PolicyRequestData policy) throws MailException, IOException, InvalidMessageException {
	MailMessageBuilder builder = mailHelper.createBuilder();
	MailMessage message = buildMessage(builder, policy);

	MailSender sender = mailHelper.createSender();
	sender.send(message);
    }

    private MailMessage buildMessage(MailMessageBuilder builder, PolicyRequestData policy)
	    throws MailException, IOException {

	MailMessage mm = builder.createMessage();
	mm.addTORecipient(builder.createAddress(DEFAULT_TO_RECIPIENTS));
	mm.setSubject(DEFAULT_SUBJECT);

	String htmlBody = getMessageBody(policy);

	MailMessagePart body = builder.createHTMLPart(htmlBody);
	mm.addPart(body);
	addImagesParts(mm, builder, policy);

	return mm;
    }

    private String getMessageBody(PolicyRequestData policy) {
	VelocityContext context = new VelocityContext();
	context.put("policy", policy);
	Writer w = new StringWriter();
	Template t = ve.getTemplate(TEMPLATE_NAME, "UTF-8");
	t.merge(context, w);
	return w.toString();
    }

    private MailMessagePart createFromUploaded(MailMessageBuilder builder, UploadedImage image)
	    throws MailException, IOException {
	MailMessagePart part = builder.createStreamPart(image.getFile().getFileName(), image.getFile().getContentType(),
		image.getFile().getInputstream(), image.getSafeId());
	return part;
    }

    private void addUploadedImagesList(MailMessage message, MailMessageBuilder builder, List<UploadedImage> images)
	    throws MailException, IOException {
	for (UploadedImage image : images)
	    message.addPart(createFromUploaded(builder, image));
    }

    private void addImagesParts(MailMessage message, MailMessageBuilder builder, PolicyRequestData policy)
	    throws MailException, IOException {
	addUploadedImagesList(message, builder, policy.getInsurant().getIdentityCardData().getScanFiles());
	for (InsuredDriverData driver : policy.getInsuredDrivers()) {
	    addUploadedImagesList(message, builder, driver.getDriverLicenseData().getScanFiles());
	    addUploadedImagesList(message, builder, driver.getIdentityCardData().getScanFiles());
	    addUploadedImagesList(message, builder, driver.getGpwParticipantCertificateData().getScanFiles());
	    addUploadedImagesList(message, builder, driver.getHandicapedCertificateData().getScanFiles());
	    addUploadedImagesList(message, builder, driver.getPensionerCertificateData().getScanFiles());
	    addUploadedImagesList(message, builder, driver.getPrivilegerCertificateData().getScanFiles());
	}
	for (InsuredVehicleData vehicle : policy.getInsuredVehicles())
	    addUploadedImagesList(message, builder, vehicle.getVehicleCertificateData().getScanFiles());
    }
}
