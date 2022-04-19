# Build docker containers

```
docker-compose build
```

# Copy/Place GitHub audit log file (in JSON format) to/in local disk location.
This file is mounted for use in the indexing container. See compose file for volume mount.

```
cp [your GH audit log json file] /tmp/audit_log.json
```

# Up docker stack

```
docker-compose up
```

# After loading is complete, logs are available in Kibana

```
http://localhost:5601/
```

# Create index pattern for analysis
- Nav Menu -> Management -> Stack Management
- Kibana -> Index Patterns -> Create Index Pattern
-- Name: "gh*"
-- Timestamp Field - "created_at"
- Click "Create Index Pattern"

# Stop docker stack
```
docker-compose down
```