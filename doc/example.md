

Injeção da propriedade

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private static final String MSG_ERROR_URL_NOT_FOUND = "There is no user for the ID '%s' provided.";
    private static final String MSG_SHORTENED_URL_CREATED = "Shortened URL created: %s";

    private static final Integer HASH_LENGHT = 6;
    private static final Integer EXPIRATION_IN_DAYS = 30;

    private final UserRepository userRepository;

    private final UrlRepository urlRepository;

    private final UrlMapper urlMapper;

    private final Validator validator;

    @Value("app-config.url-shortener.hash-lenght")
    private final Integer configHashLenht;

    @Value("app-config.url-shortener.expiration-in-days")
    private final Integer configExpiration;


    // dando erro ....

}

```