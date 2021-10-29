package kr.carrot.springsecurity.app.service;

import kr.carrot.springsecurity.app.dto.request.AuthorizationRequestDto;
import kr.carrot.springsecurity.app.dto.request.ClientInfoRequestDto;
import kr.carrot.springsecurity.app.dto.response.AuthorizationResponseDto;
import kr.carrot.springsecurity.app.dto.response.ClientInfoResponseDto;
import kr.carrot.springsecurity.app.entity.AuthorizationCodeEntity;
import kr.carrot.springsecurity.app.entity.ClientEntity;
import kr.carrot.springsecurity.app.repository.AuthorizationCodeRepository;
import kr.carrot.springsecurity.app.repository.ClientRepository;
import kr.carrot.springsecurity.security.exception.oauth2.InvalidRequestException;
import kr.carrot.springsecurity.security.exception.oauth2.Oauth2Exception;
import kr.carrot.springsecurity.security.exception.oauth2.UnSupportedResponseTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    public static final String RESPONSE_TYPE_CODE = "code";

    private final ClientRepository clientRepository;
    private final AuthorizationCodeRepository authorizationCodeRepository;

    public ClientInfoResponseDto saveClient(ClientInfoRequestDto clientInfo) {

        String clientId = clientInfo.getClientId();
        if (clientRepository.findByClientId(clientId).isPresent()) {
            throw new RuntimeException("이미 client id가 존재합니다."); // todo: change exception type
        }

        String uuid = UUID.randomUUID().toString();

        ClientEntity entity = ClientEntity.builder()
                .clientId(clientInfo.getClientId())
                .redirectUri(clientInfo.getRedirectUri())
                .clientSecret(uuid)
                .build();

        clientRepository.save(entity);

        return new ClientInfoResponseDto(entity.getClientId(), entity.getClientSecret(), entity.getRedirectUri());
    }

    public ClientInfoResponseDto retrieveClient(String clientId) {

        ClientEntity entity = clientRepository.findByClientId(clientId)
                .orElseThrow();

        return new ClientInfoResponseDto(entity.getClientId(), entity.getClientSecret(), entity.getRedirectUri());
    }

    public void authorize(AuthorizationRequestDto requestDto) {

        // 필수값이 없는 경우
        if (!StringUtils.hasText(requestDto.getResponseType())
                || !StringUtils.hasText(requestDto.getClientId())
                || !StringUtils.hasText(requestDto.getRedirectUri())) {
            throw new InvalidRequestException();
        }

        // response type = code
        if (!RESPONSE_TYPE_CODE.equals(requestDto.getResponseType())) {
            throw new UnSupportedResponseTypeException();
        }

        ClientEntity clientEntity = clientRepository.findByClientId(requestDto.getClientId())
                .orElseThrow(InvalidRequestException::new);

        if (!clientEntity.getRedirectUri().equals(requestDto.getRedirectUri())) {
            throw new InvalidRequestException();
        }
    }
}
