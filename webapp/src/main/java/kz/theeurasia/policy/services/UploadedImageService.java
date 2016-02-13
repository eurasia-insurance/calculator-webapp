package kz.theeurasia.policy.services;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import kz.theeurasia.policy.domain.UploadedImage;

@ManagedBean
@SessionScoped
public class UploadedImageService implements Serializable {
    private static final long serialVersionUID = 4795788265713605816L;

    private Map<UUID, UploadedImage> filesMap;
    private List<UploadedImage> filesList;
    private List<UploadedImage> last;

    @PostConstruct
    public void init() {
	filesMap = new HashMap<>();
	filesList = new ArrayList<>();
	last = new ArrayList<>();
    }

    public final void onImageUpload(FileUploadEvent event) {
	UploadedImage im = new UploadedImage(event.getFile());
	filesMap.put(im.getId(), im);
	filesList.add(im);
	last.add(im);
    }

    public List<UploadedImage> pickUpLast() {
	List<UploadedImage> res = new ArrayList<>(last);
	last.clear();
	return res;
    }

    public void remove(UploadedImage image) {
	filesMap.remove(image.getId());
	filesList.remove(image);
	last.remove(image);
    }

    public final List<UploadedImage> getAllImages() {
	return new ArrayList<>(filesList);
    }

    public final StreamedContent getContent() throws IOException {
	FacesContext context = FacesContext.getCurrentInstance();
	if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
	    return new DefaultStreamedContent();
	String stringId = context.getExternalContext().getRequestParameterMap().get("id");
	UUID id = UUID.fromString(stringId);
	return new DefaultStreamedContent(filesMap.get(id).getFile().getInputstream());
    }

}