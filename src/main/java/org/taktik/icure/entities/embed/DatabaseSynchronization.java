/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.taktik.icure.entities.embed;

import java.io.Serializable;
import java.util.Objects;

public class DatabaseSynchronization implements Serializable {
	protected String source;
	protected String target;
	protected String filter;

	public DatabaseSynchronization() {
	}

	public DatabaseSynchronization(String source, String target) {
		this(source,target,null);
	}
	public DatabaseSynchronization(String source, String target, String filter) {
		this.source = source;
		this.target = target;
		this.filter = filter;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public String getFilter() {
		return filter;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DatabaseSynchronization that = (DatabaseSynchronization) o;
		return Objects.equals(source, that.source) &&
				Objects.equals(target, that.target);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source, target);
	}
}