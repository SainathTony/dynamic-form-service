#!/bin/bash

# Docker script to create PostgreSQL database for dynamic-form-service

echo "Creating PostgreSQL container for dynamic-form-service..."

# Stop and remove existing container if it exists
docker stop dynamic-form-postgres 2>/dev/null || true
docker rm dynamic-form-postgres 2>/dev/null || true

# Create and run PostgreSQL container
docker run -d \
  --name dynamic-form-postgres \
  -e POSTGRES_DB=dynamic_form_db \
  -e POSTGRES_USER=dynamic_user \
  -e POSTGRES_PASSWORD=dynamic_password \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:15-alpine

echo "Waiting for PostgreSQL to be ready..."
sleep 10

# Create additional database user with specific permissions (optional)
docker exec -i dynamic-form-postgres psql -U dynamic_user -d dynamic_form_db << 'EOF'
-- Grant all privileges to the user
GRANT ALL PRIVILEGES ON DATABASE dynamic_form_db TO dynamic_user;

-- Create extension for UUID if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Display connection info
SELECT 'Database setup completed successfully!' as status;
EOF

echo "PostgreSQL container 'dynamic-form-postgres' is running on port 5432"
echo "Database: dynamic_form_db"
echo "Username: dynamic_user" 
echo "Password: dynamic_password"
echo ""
echo "Connection URL: jdbc:postgresql://localhost:5432/dynamic_form_db"
echo ""
echo "To connect using psql:"
echo "docker exec -it dynamic-form-postgres psql -U dynamic_user -d dynamic_form_db"