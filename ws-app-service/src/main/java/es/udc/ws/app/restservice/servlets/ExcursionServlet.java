package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.ExcursionNotUpdateableDatesException;
import es.udc.ws.app.model.appservice.exceptions.ExcursionNotUpdateablePlacesException;
import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.restservice.dto.ExcursionToRestExcursionDtoConversor;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestExcursionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcursionServlet extends RestHttpServletTemplate {

    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        ServletUtils.checkEmptyPath(req);
        RestExcursionDto excursionDto= JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        Excursion excursion= ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        excursion = AppServiceFactory.getService().createExcursion(excursion);

        excursionDto=ExcursionToRestExcursionDtoConversor.toRestExcursionDto(excursion);
        String excursionURL= ServletUtils.normalizePath(req.getRequestURL().toString()+"/"+excursion.getExcursionId());
        Map<String,String> headers=new HashMap<>(1);
        headers.put("Location",excursionURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestExcursionDtoConversor.toObjectNode(excursionDto), headers);

    }


    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, InstanceNotFoundException, InputValidationException {
        Long excursionId= ServletUtils.getIdFromPath(req,"excursion");

        RestExcursionDto excursionDto=JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        if(!excursionId.equals(excursionDto.getExcursionId())){
            throw new InputValidationException("Invalid request: invalid excursion in excursion id:"+excursionId);
        }
        Excursion excursion=ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);
        try {
            AppServiceFactory.getService().updateExcursion(excursion);

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);

        } catch (ExcursionNotUpdateablePlacesException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toExcursionNotEnoughPlacesException(e), null);
        }catch (ExcursionNotUpdateableDatesException e){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toExcursionLateDateException(e), null);
        }

    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String city = req.getParameter("city");
        String early = req.getParameter("earlyDate");
        String late = req.getParameter("lateDate");
        LocalDateTime earlyDate = LocalDateTime.parse(early, formatter);
        LocalDateTime lateDate = LocalDateTime.parse(late, formatter);

        List<Excursion> excursions = AppServiceFactory.getService().findExcursions(city, earlyDate, lateDate);

        List<RestExcursionDto> excursionDtos = ExcursionToRestExcursionDtoConversor.toRestExcursionDtos(excursions);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestExcursionDtoConversor.toArrayNode(excursionDtos), null);
    }

}
