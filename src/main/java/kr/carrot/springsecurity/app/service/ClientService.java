package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.request.AuthorizationRequestDto;
import kr.carrot.springsecurity.app.dto.request.ClientInfoRequestDto;
import kr.carrot.springsecurity.app.dto.response.AuthorizationResponseDto;
import kr.carrot.springsecurity.app.dto.response.ClientInfoResponseDto;
import kr.carrot.springsecurity.app.entity.ClientEntity;
import kr.carrot.springsecurity.app.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientInfoResponseDto saveClient(ClientInfoRequestDto clientInfo) {

        String uuid = UUID.randomUUID().toString();

        ClientEntity entity = ClientEntity.builder()
                .clientId(clientInfo.getClientId())
                .redirectUri(clientInfo.getRedirectUri())
                .clientSecret(uuid)
                .build();

        clientRepository.save(entity);

        return new ClientInfoResponseDto(entity.getClientId(), entity.getClientSecret(), entity.getRedirectUri());
    }

    public AuthorizationResponseDto authorize(AuthorizationRequestDto requestDto) {
        // todo: fill method

        return null;
    }
}
