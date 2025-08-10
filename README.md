# Dynamic Form Service

A Spring Boot application that generates dynamic forms using AI and stores them in a PostgreSQL database. The application uses Groq API for intelligent form generation based on natural language input.

## Features

- **AI-Powered Form Generation**: Generate forms from natural language descriptions using Groq API
- **Dynamic Form Fields**: Support for various field types (text, number, email, date, dropdown, radio, textarea)
- **Dropdown Options**: AI automatically generates appropriate options for dropdown and radio fields
- **Form Submission**: Save and retrieve form submissions with search functionality
- **RESTful API**: Complete REST API for form management
- **Database Integration**: PostgreSQL database with JPA/Hibernate

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Groq API key (sign up at [groq.com](https://groq.com))

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/SainathTony/dynamic-form-service.git
cd dynamic-form-service
```

### 2. Set up Environment Variables

Copy the example environment file:

```bash
cp .env.example .env
```

Edit `.env` and add your Groq API key:

```bash
# Environment variables for local development
GROQ_API_KEY=your_actual_groq_api_key_here
```

**Important**: Never commit your `.env` file to version control. It contains sensitive information.

### 3. Set up PostgreSQL Database

The project includes a Docker script to set up PostgreSQL. Run:

```bash
run docker-postgres.bat if you are using windows
run docker-postgres.sh if you are using mac or lynux
```

This will start a PostgreSQL container with the following configuration:
- **Host**: localhost
- **Port**: 5432
- **Database**: dynamic_form_db
- **Username**: dynamic_user
- **Password**: dynamic_password

### 4. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## API Endpoints

### Form Generation

**POST** `/api/forms/generate`

Generate a form from natural language input.

```bash
curl -X POST http://localhost:8080/api/forms/generate \
  -H "Content-Type: application/json" \
  -d '{
    "form_input": "Create a cricket team registration form with team name, captain details, contact info, and team category"
  }'
```

### Get All Forms

**GET** `/api/forms`

```bash
curl http://localhost:8080/api/forms
```

### Submit Form

**POST** `/api/forms/submit`

```bash
curl -X POST http://localhost:8080/api/forms/submit \
  -H "Content-Type: application/json" \
  -d '{
    "form_id": 1,
    "form_data": {
      "1": "Team Eagles",
      "2": "John Doe",
      "3": "john@example.com"
    }
  }'
```

### Get Form Submissions

**GET** `/api/forms/{formId}/submissions`

```bash
# Get all submissions
curl http://localhost:8080/api/forms/1/submissions

# Search submissions
curl "http://localhost:8080/api/forms/1/submissions?searchTerm=Eagles"
```

## Database Schema

The application uses the following main tables:

- `forms` - Stores form definitions
- `form_fields` - Stores individual form fields with options
- `form_submissions` - Stores form submission records
- `form_submission_fields` - Stores individual field values

## Environment Configuration

### Development (.env file)

```bash
# Groq API Configuration
GROQ_API_KEY=your_groq_api_key_here

```

## AI Form Generation

The application uses Groq's AI models to generate forms from natural language. The AI automatically:

- Creates appropriate field types based on context
- Generates relevant options for dropdown and radio fields
- Sets proper validation rules and requirements
- Creates user-friendly placeholders

### Example Inputs and Generated Fields

**Input**: "Create a job application form"

**Generated Fields**:
- Full Name (text)
- Email (email)
- Phone Number (text)
- Position Applied For (dropdown with options)
- Experience Level (dropdown: Beginner, Intermediate, Advanced, Expert)
- Resume Upload (file)
- Cover Letter (textarea)

## Development

### Running Tests

```bash
./gradlew test
```

### Building the Application

```bash
./gradlew build
```

### Database Migration

The application uses Hibernate's `ddl-auto=update` for development. For production, consider using Flyway or Liquibase for proper database migrations.

## Project Structure

```
src/
├── main/
│   ├── java/com/dynamic_form/service/
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access layer
│   │   ├── entity/              # JPA entities
│   │   ├── dto/                 # Data transfer objects
│   │   ├── config/              # Configuration classes
│   │   └── exception/           # Custom exceptions
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/                        # Unit and integration tests
```

## Security

- API keys are stored in environment variables
- `.env` file is ignored by Git
- Database credentials should be secured in production
- Consider implementing authentication for production use

## Troubleshooting

### Common Issues

1. **Port 8080 already in use**
   ```bash
   ./gradlew bootRun --args="--server.port=8081"
   ```

2. **Database connection error**
   - Ensure PostgreSQL container is running: `docker ps`
   - Check connection settings in `application.properties`

3. **Missing API key error**
   - Verify `.env` file exists and contains `GROQ_API_KEY`
   - Check that the API key is valid

4. **Form generation failures**
   - Verify Groq API key is working
   - Check application logs for detailed error messages

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

[Add your license information here]

## Support

For issues and questions:
- Create an issue in the repository
- Check the application logs for detailed error information
- Verify environment configuration