package org.phoenixctms.ctsms.web.component.photocam;

import java.util.List;

import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.MetaRuleset;

import org.primefaces.event.CaptureEvent;
import org.primefaces.facelets.MethodRule;

public class PhotoCamTagHandler extends ComponentHandler {

	public PhotoCamTagHandler(ComponentConfig config) {
		super(config);
	}

	@Override
	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);
		metaRuleset.addRule(new MethodRule("listener", List.class, new Class[] { CaptureEvent.class }));
		return metaRuleset;
	}
}
