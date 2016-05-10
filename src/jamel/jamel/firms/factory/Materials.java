package jamel.jamel.firms.factory;

import jamel.jamel.widgets.Asset;

/**
 * Represents a heap of materials.
 */
interface Materials extends Asset {

	/**
	 * Adds the specified stuff to this heap.
	 * 
	 * @param stuff
	 *            the stuff to be added.
	 */
	void add(Materials stuff);

	/**
	 * Deletes this heap of materials.
	 */
	void delete();

	/**
	 * Delete a fraction of this heap of materials.
	 * 
	 * @param volume
	 *            the volume to be deleted.
	 * @param value
	 *            the value to be deleted.
	 */
	void delete(long volume, long value);

	/**
	 * Returns the completion of the materials in this heap.
	 * 
	 * @return the completion of the materials.
	 */
	Rational getCompletion();

	/**
	 * Returns when the materials in this heap were produced.
	 * 
	 * @return when the materials in this heap were produced.
	 */
	int getProductionPeriod();

	/**
	 * Returns the type of the materials.
	 * 
	 * @return the type of the materials.
	 */
	String getType();

	/**
	 * Returns the volume of this heap.
	 * 
	 * @return the volume of this heap.
	 */
	long getVolume();

	/**
	 * Sets the value of this heap of materials.
	 * 
	 * @param newValue
	 *            the value of this heap of materials.
	 */
	void setValue(long newValue);

}

// ***
