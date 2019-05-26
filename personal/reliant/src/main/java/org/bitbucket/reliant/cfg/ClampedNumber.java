package org.bitbucket.reliant.cfg;

import java.util.Comparator;

public final class ClampedNumber<N extends Number & Comparable<N>> extends Number implements Comparable<ClampedNumber<?>> {
	/**
	 * The default serial version UID.
	 */
	private static final long serialVersionUID = 1L;
	private final Comparator<N> limitCmp = new Comparator<N>() {
		@Override
		public int compare(N o1, N o2) {
			// TODO Auto-generated method stub
			return o1.compareTo(o2);
		}
	};
	private final N min, max;
	private N value;

	public ClampedNumber(final N value, final N min, final N max) {
		this.min = min;
		this.max = max;
		setValue(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(ClampedNumber<?> o) {
		// TODO Auto-generated method stub
		final N fromVal = getValue();
		final Number toVal = o.getValue();
		return fromVal.getClass() == toVal.getClass() ? fromVal.compareTo((N) toVal) : 0;
	}

	@Override
	public double doubleValue() {
		// TODO Auto-generated method stub
		return getValue().doubleValue();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof ClampedNumber<?> && ((ClampedNumber<?>) obj).getValue().equals(getValue());
	}

	@Override
	public float floatValue() {
		// TODO Auto-generated method stub
		return getValue().floatValue();
	}

	public N getMax() {
		return max;
	}

	public N getMin() {
		return min;
	}

	public N getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final N value = getValue();
		return 31 * (value == null ? 0 : value.hashCode());
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return getValue().intValue();
	}

	@Override
	public long longValue() {
		// TODO Auto-generated method stub
		return getValue().longValue();
	}

	public void setValue(N value) {
		if (limitCmp.compare(value, min) < 0) {
			value = min;
		} else if (limitCmp.compare(value, max) > 0) {
			value = max;
		}
		this.value = value;
	}

	@Override
	public String toString() {
		return String.valueOf(getValue());
	}
}