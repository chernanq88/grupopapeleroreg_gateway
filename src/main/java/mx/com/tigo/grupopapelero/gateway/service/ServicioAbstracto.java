package mx.com.tigo.grupopapelero.gateway.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import lombok.Data;
import mx.com.tigo.transfer.Adjunto;
import mx.com.tigo.transfer.SolicitudCorreoInfo;
import mx.com.tigo.transfer.interfaces.IServicioEnvioCorreos;

@Data
public class ServicioAbstracto {
	
	@Value(value = "${grupopapeleroreg.smtp.user}")
	private String mailUser;
	
	@Value(value = "${grupopapeleroreg.smtp.password}")
	private String mailPassword;

	@Value(value = "${grupopapeleroreg.smtp.host}")
	private String mailHost;

	@Value(value = "${grupopapeleroreg.smtp.port}")
	private String mailPort;
	
	@Value(value = "${grupopapeleroreg.url.static}")
	private String rutaContenidoEstatico;

	@Autowired
	private IServicioEnvioCorreos servicioMensajeria;
	

	protected void enviarCorreo(String para, String asunto, String cuerpo, String adjuntoBase64, String nombreAdjunto) {

		
		this.mailUser="no-reply@triver.mx";
		this.mailHost="smtp.office365.com";
		this.mailPort="587";
		this.mailPassword="Coy42765";
		
		SolicitudCorreoInfo solicitud = new SolicitudCorreoInfo();
		solicitud.setAsunto(asunto);
		solicitud.setPara(para);
		solicitud.setUsuarioSaliente(getMailUser());
		solicitud.setContrasenaSaliente(getMailPassword());
		solicitud.setHost(getMailHost());
		solicitud.setPuerto(getMailPort());
		solicitud.setDe(getMailUser());
		solicitud.setAuth(true);
		solicitud.setSsl(true);
		solicitud.setStarttls(true);
		solicitud.setCuerpo(replaceAcutesHTML(cuerpo));
		solicitud.setAdjuntos(new ArrayList<Adjunto>());

		if (adjuntoBase64 == null) {
			servicioMensajeria.enviarCorreo(solicitud);
		} else {
			solicitud.getAdjuntos().add(new Adjunto(adjuntoBase64, nombreAdjunto));
			servicioMensajeria.enviarCorreoConAdjuntos(solicitud);
		}

	}

	protected void enviarCorreoAdjuntos(String para, String asunto, String cuerpo, String adjuntoBase64,
			String adjuntoBase642, String nombreAdjunto, String nombreAdjunto2) {
		SolicitudCorreoInfo solicitud = new SolicitudCorreoInfo();
		solicitud.setAsunto(asunto);
		solicitud.setPara(para);
		solicitud.setUsuarioSaliente(getMailUser());

		solicitud.setContrasenaSaliente(mailPassword);
		solicitud.setHost(getMailHost());
		solicitud.setPuerto(getMailPort());
		solicitud.setDe(getMailUser());
		solicitud.setAuth(true);
		solicitud.setSsl(true);
		solicitud.setCuerpo(replaceAcutesHTML(cuerpo));
		solicitud.setAdjuntos(new ArrayList<Adjunto>());
		if (adjuntoBase64 == null) {
			servicioMensajeria.enviarCorreo(solicitud);
		} else {
			solicitud.getAdjuntos().add(new Adjunto(adjuntoBase64, nombreAdjunto));
			if (adjuntoBase642 != null)
				solicitud.getAdjuntos().add(new Adjunto(adjuntoBase642, nombreAdjunto2));
			servicioMensajeria.enviarCorreoConAdjuntos(solicitud);
		}

	}

	protected String replaceAcutesHTML(String str) {
		return str.replace("á", "&aacute;").replace("é", "&eacute;").replace("í", "&iacute;").replace("ó", "&oacute;")
				.replace("ú", "&uacute;").replace("Á", "&Aacute;").replace("É", "&Eacute;").replace("Í", "&Iacute;")
				.replace("Ó", "&Oacute;").replace("Ú", "&Uacute;").replace("ñ", "&ntilde;").replace("Ñ", "&Ntilde;");
	}

	protected String getClientIP(HttpServletRequest request) {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
