package com.jeanbarcellos.project111.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.jeanbarcellos.project111.dto.UrlRequest;
import com.jeanbarcellos.project111.dto.UrlResponse;
import com.jeanbarcellos.project111.entity.Url;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UrlMapper {

    private final ModelMapper modelMapper;

    public Url toEntity(UrlRequest request) {
        return this.modelMapper.map(request, Url.class);
    }

    public UrlResponse toResponse(Url user) {
        return this.modelMapper.map(user, UrlResponse.class);
    }

    public List<UrlResponse> toResponseList(List<Url> users) {
        return users.stream()
                .map(this::toResponse)
                .toList();
    }

    public Url copy(Url destination, UrlRequest source) {
        this.modelMapper.map(source, destination);
        return destination;
    }

}
