package org.phoenixctms.ctsms.web.model;

import org.phoenixctms.ctsms.enumeration.RandomizationMode;

public interface RandomizationModeSelectorListener {

	public final static RandomizationMode NO_SELECTION_RANDOMIZATION_MODE = null;

	public RandomizationMode getRandomizationMode(int property);

	public void setRandomizationMode(int property, RandomizationMode mode);
}
