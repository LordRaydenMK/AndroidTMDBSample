FROM wiremock/wiremock:latest

# Copy mappings and __files into WireMock's default directories
COPY mappings /home/wiremock/mappings
COPY __files /home/wiremock/__files

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh", "--global-response-templating", "--disable-gzip", "--verbose"]