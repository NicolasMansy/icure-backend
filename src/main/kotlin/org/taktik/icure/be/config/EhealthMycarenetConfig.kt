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

package org.taktik.icure.be.config

import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.GsonFactoryBean
import org.taktik.icure.be.ehealth.logic.crypto.impl.CryptoEteeImpl
import org.taktik.icure.be.ehealth.logic.kmehr.smf.impl.v2_3g.SoftwareMedicalFileExport
import org.taktik.icure.be.ehealth.logic.kmehr.smf.impl.v2_3g.SoftwareMedicalFileLogicImpl
import org.taktik.icure.be.ehealth.logic.kmehr.sumehr.impl.v20110701.SumehrExport
import org.taktik.icure.be.ehealth.logic.kmehr.sumehr.impl.v20110701.SumehrLogicImpl
import org.taktik.icure.dao.impl.idgenerators.UUIDGenerator
import org.taktik.icure.logic.impl.filter.Filters
import org.taktik.icure.services.external.rest.v1.transformationhandlers.V1MapperFactory

@Configuration
class EhealthMycarenetConfig {
    @Bean fun sumehrLogic() = SumehrLogicImpl()
    @Bean fun sumehrExport() = SumehrExport()
    @Bean fun softwareMedicalFileLogic() = SoftwareMedicalFileLogicImpl()
    @Bean fun softwareMedicalFileExport() = SoftwareMedicalFileExport()
    @Bean fun cryptoEtee() = CryptoEteeImpl()
}