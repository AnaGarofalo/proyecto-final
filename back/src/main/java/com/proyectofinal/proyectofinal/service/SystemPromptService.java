package com.proyectofinal.proyectofinal.service;

import com.proyectofinal.proyectofinal.dto.SystemPromptDTO;
import com.proyectofinal.proyectofinal.model.SystemPrompt;
import com.proyectofinal.proyectofinal.repository.SystemPromptRepository;
import com.proyectofinal.proyectofinal.exception.PFNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SystemPromptService extends AbstractService<SystemPrompt, SystemPromptRepository>{
  /*
   * // TODO: hacer esto editable y que se guarde en la bdd
   * private static final String EDITABLE_PROMPT = """
   * Sos el asistente virtual interno de Nestlé.
   * Tu función principal es ayudar a los empleados respondiendo sus preguntas
   ** únicamente** con la información disponible en el contexto proporcionado.
   * Si la respuesta no está en el contexto, respondé con "No lo sé".
   * 
   * También cumplís el rol de asistente del área de mantenimiento:
   * recibís reportes de problemas y, cuando corresponda, generás tickets
   * para solucionarlos.
   * 
   * REGLAS PARA CREAR TICKETS:
   * - Para generar un ticket necesitás tres datos obligatorios:
   * El problema a reportar
   * El edificio donde ocurre
   * La confirmación expresa del usuario de que estos datos son correctos
   * - Antes de confirmar un ticket:
   * Hacé un resumen al usuario con la información que entendiste.
   * El ticket debe ser lo más específico posible, podés repreguntarle al usuario
   * si encontrás que te falta información.
   * Preguntale si el ticket es correcto.
   * - Después de confirmar un ticket:
   * Sólo después de confirmar los datos, estás autorizado a generar el ticket
   * hasta que le hayas pedido confirmación expresa y te haya respondido
   * afirmativamente.
   * Una vez que el usuario confirmó los datos, cierra la conversación con una
   * despedida amistosa
   *** 
   * CRÍTICO***
   * Sólo podés generar el contenido del ticket una vez que le preguntaste al
   * usuario si los datos eran correctos y te lo confirmó explícitamente
   * """;
   * 
   * // Sección 2: Formato (no editable)
   * private static final String FORMATTING_PROMPT_TEMPLATE = """
   * FORMATO DE RESPUESTA:
   * 1. Siempre devolvé la respuesta en formato JSON válido, sin texto adicional.
   * 2. El JSON debe tener las siguientes claves (case sensitive):
   * %s
   * 3. El contenido del ticket debe estar redactado como un párrafo coherente y
   * claro.
   * """;
   * 
   * private static String buildKeysList() {
   * return java.util.Arrays.stream(IAResponseKeys.values())
   * .map(key -> "- " + key.name() + ": " + key.getDefinition())
   * .collect(Collectors.joining("\n               "));
   * }
   * 
   * 
   * public String getBasePrompt() {
   * return EDITABLE_PROMPT
   * + "\n\n"
   * + FORMATTING_PROMPT_TEMPLATE.formatted(buildKeysList());
   * }
   */

  public SystemPromptService(SystemPromptRepository systemPromptRepository) {
    super(systemPromptRepository, SystemPrompt.class);
  }

  public SystemPromptDTO getLatest() {
    List<SystemPrompt> all = repository.findAll();
    if (all.isEmpty())
      return null;
    SystemPrompt last = all.get(all.size() - 1);
    return new SystemPromptDTO(last.getPrompt(), last.getTicketEmail());
  }

  public Optional<SystemPrompt> findByTicketEmail(String ticketEmail) {
    return repository.findByTicketEmail(ticketEmail);
  }

  public SystemPrompt create(SystemPromptDTO dto) {
    SystemPrompt entity = SystemPrompt.builder()
        .prompt(dto.getPrompt())
        .ticketEmail(dto.getTicketEmail())
        .build();
    return repository.save(entity);
  }

  public SystemPromptDTO update(String prompt, String ticketEmail) {
    // Buscar la entidad existente por ticketEmail usando el método del servicio.
    SystemPrompt existing = findByTicketEmail(ticketEmail)
        .orElseThrow(() -> new PFNotFoundException(ticketEmail, "ticketEmail", SystemPrompt.class));

    // Actualizar los campos de la entidad encontrada
    existing.setPrompt(prompt);
    existing.setTicketEmail(ticketEmail);

    SystemPrompt saved = repository.save(existing);
    return new SystemPromptDTO(saved.getPrompt(), saved.getTicketEmail());
  }
}
