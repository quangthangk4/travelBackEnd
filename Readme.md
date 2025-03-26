## create redis container
docker run --name redis-travel -v my-data-redis:/usr/local/etc/redis -dp 6379:6379 redis:7.4.2
