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

package org.taktik.icure.services.external.rest.v1.facade.be

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component
import org.taktik.icure.be.ehealth.logic.kmehr.smf.SoftwareMedicalFileLogic
import org.taktik.icure.be.ehealth.logic.kmehr.sumehr.SumehrLogic
import org.taktik.icure.entities.HealthElement
import org.taktik.icure.entities.HealthcareParty
import org.taktik.icure.entities.embed.Service
import org.taktik.icure.logic.HealthcarePartyLogic
import org.taktik.icure.logic.PatientLogic
import org.taktik.icure.logic.SessionLogic
import org.taktik.icure.services.external.rest.v1.dto.HealthElementDto
import org.taktik.icure.services.external.rest.v1.dto.HealthcarePartyDto
import org.taktik.icure.services.external.rest.v1.dto.be.kmehr.SoftwareMedicalFileExportDto
import org.taktik.icure.services.external.rest.v1.dto.be.kmehr.SumehrContentDto
import org.taktik.icure.services.external.rest.v1.dto.be.kmehr.SumehrExportInfoDto
import org.taktik.icure.services.external.rest.v1.dto.be.kmehr.SumehrStatus
import org.taktik.icure.services.external.rest.v1.dto.be.kmehr.SumehrValidityDto
import org.taktik.icure.services.external.rest.v1.dto.embed.ContentDto
import org.taktik.icure.services.external.rest.v1.dto.embed.ServiceDto
import org.taktik.icure.utils.ResponseUtils
import java.util.Arrays
import java.util.stream.Collectors
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response
import javax.ws.rs.core.StreamingOutput

@Component
@Path("/be_kmehr")
@Api(tags = ["be_kmehr"])
@Consumes("application/json")
@Produces("application/json")
class KmehrFacade(val mapper: MapperFacade, val sessionLogic: SessionLogic, val sumehrLogic: SumehrLogic, val softwareMedicalFileLogic: SoftwareMedicalFileLogic, val healthcarePartyLogic: HealthcarePartyLogic, val patientLogic: PatientLogic) {
    @ApiOperation(value = "Generate sumehr", httpMethod = "POST", notes = "")
    @POST
    @Path("/sumehr/{patientId}/export")
    @Produces("application/octet-stream")
    fun generateSumehr(@PathParam("patientId") patientId: String, @QueryParam("language") language: String, info: SumehrExportInfoDto): Response {
        return ResponseUtils.ok(StreamingOutput { output -> sumehrLogic.createSumehr(output!!, patientLogic.getPatient(patientId), info.secretForeignKeys, healthcarePartyLogic.getHealthcareParty(sessionLogic.currentSessionContext.user.healthcarePartyId), mapper!!.map<HealthcarePartyDto, HealthcareParty>(info.recipient, HealthcareParty::class.java), language, info.comment, null) })
    }

	@ApiOperation(value = "Generate sumehr", httpMethod = "POST", notes = "")
	@POST
	@Path("/sumehrpp/{patientId}/export")
	@Produces("application/octet-stream")
	fun generateSumehrPlusPlus(@PathParam("patientId") patientId: String, @QueryParam("language") language: String, info: SumehrExportInfoDto): Response {
		return ResponseUtils.ok(StreamingOutput { output -> sumehrLogic.createSumehrPlusPlus(output!!, patientLogic.getPatient(patientId), info.secretForeignKeys, healthcarePartyLogic.getHealthcareParty(sessionLogic.currentSessionContext.user.healthcarePartyId), mapper!!.map<HealthcarePartyDto, HealthcareParty>(info.recipient, HealthcareParty::class.java), language, info.comment, null) })
	}

	@ApiOperation(value = "Validate sumehr", httpMethod = "POST", notes = "")
    @POST
    @Path("/sumehr/{patientId}/validate")
    @Produces("application/octet-stream")
    fun validateSumehr(@PathParam("patientId") patientId: String, @QueryParam("language") language: String, info: SumehrExportInfoDto): Response {
        return ResponseUtils.ok(StreamingOutput { output -> sumehrLogic.validateSumehr(output!!, patientLogic.getPatient(patientId), info.secretForeignKeys, healthcarePartyLogic.getHealthcareParty(sessionLogic.currentSessionContext.user.healthcarePartyId), mapper!!.map<HealthcarePartyDto, HealthcareParty>(info.recipient, HealthcareParty::class.java), language, info.comment, null) } as StreamingOutput)
    }

	@ApiOperation(value = "Get sumehr elements", response = SumehrContentDto::class, httpMethod = "POST", notes = "")
	@POST
	@Path("/sumehr/{patientId}/content")
	fun getSumehrContent(@PathParam("patientId") patientId: String, info: SumehrExportInfoDto): Response {
		val result = SumehrContentDto()

		result.services = sumehrLogic.getAllServices(sessionLogic.currentSessionContext.user.healthcarePartyId, info.secretForeignKeys, null).stream().map { s -> mapper!!.map<Service, ServiceDto>(s, ServiceDto::class.java) }.collect(Collectors.toList<ServiceDto>())
		result.healthElements = sumehrLogic.getHealthElements(sessionLogic.currentSessionContext.user.healthcarePartyId, info.secretForeignKeys).stream().map { h -> mapper!!.map<HealthElement, HealthElementDto>(h, HealthElementDto::class.java) }.collect(Collectors.toList<HealthElementDto>())

		return ResponseUtils.ok(result)
	}

	@ApiOperation(value = "Get sumehr elements", response = SumehrContentDto::class, httpMethod = "POST", notes = "")
	@POST
	@Path("/sumehrpp/{patientId}/content")
	fun getSumehrContentPlusPlus(@PathParam("patientId") patientId: String, info: SumehrExportInfoDto): Response {
		val result = SumehrContentDto()

		result.services = sumehrLogic.getAllServicesPlusPlus(sessionLogic.currentSessionContext.user.healthcarePartyId, info.secretForeignKeys, null).stream().map { s -> mapper!!.map<Service, ServiceDto>(s, ServiceDto::class.java) }.collect(Collectors.toList<ServiceDto>())
		result.healthElements = sumehrLogic.getHealthElements(sessionLogic.currentSessionContext.user.healthcarePartyId, info.secretForeignKeys).stream().map { h -> mapper!!.map<HealthElement, HealthElementDto>(h, HealthElementDto::class.java) }.collect(Collectors.toList<HealthElementDto>())

		return ResponseUtils.ok(result)
	}

	@ApiOperation(value = "Check sumehr signature", response = ContentDto::class, httpMethod = "GET", notes = "")
	@GET
	@Path("/sumehr/{patientId}/md5")
    fun getSumehrMd5(@PathParam("patientId") patientId: String, @QueryParam("hcPartyId") hcPartyId: String, @QueryParam("secretFKeys") secretFKeys: String): Response {
        return ResponseUtils.ok(ContentDto.fromStringValue(sumehrLogic.getSumehrMd5(hcPartyId, patientLogic.getPatient(patientId), Arrays.asList(*secretFKeys.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))))
    }

	@ApiOperation(value = "Get sumehr validity", response = String::class, httpMethod = "GET", notes = "")
	@GET
	@Path("/sumehr/{patientId}/valid")
	fun isSumehrValid(@PathParam("patientId") patientId: String, @QueryParam("hcPartyId") hcPartyId: String, @QueryParam("secretFKeys") secretFKeys: String): Response {
		return ResponseUtils.ok(SumehrValidityDto(SumehrStatus.valueOf(sumehrLogic.isSumehrValid(hcPartyId, patientLogic.getPatient(patientId), Arrays.asList(*secretFKeys.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())).name)))
	}

	@ApiOperation(value = "Get SMF (Software Medical File) export")
	@POST
	@Path("/smf/{patientId}/export")
	@Produces("application/octet-stream")
	fun generateSmfExport(@PathParam("patientId") patientId: String, @QueryParam("language") language: String?, smfExportParams: SoftwareMedicalFileExportDto) : Response {
		val userHealthCareParty = healthcarePartyLogic.getHealthcareParty(sessionLogic.currentSessionContext.user.healthcarePartyId)
		return ResponseUtils.ok(StreamingOutput { output -> softwareMedicalFileLogic.createSmfExport(output!!, patientLogic.getPatient(patientId), smfExportParams.secretForeignKeys, userHealthCareParty, language ?: "fr", null) })
	}

}