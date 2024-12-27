package ru.practicum.compilation.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilation.model.Compilation;

@UtilityClass
public class CompilationMapper {
    public static Compilation mapToCompilation(NewCompilationDto dto) {
        Compilation compilation = new Compilation();
        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.getPinned());

        return compilation;
    }

    public static ResponseCompilationDto mapToResponseCompilation(Compilation compilation) {
        return new ResponseCompilationDto(compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                compilation.getEvents());
    }
}
