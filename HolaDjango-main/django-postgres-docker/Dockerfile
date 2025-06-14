FROM python:3.10

# Set environment variables
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# Set the working directory
WORKDIR /app

# Install system dependencies (INCLUDING netcat desde el principio)
RUN apt-get update && apt-get install -y \
    libpq-dev \
    netcat-openbsd \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Copy the requirements file and install Python dependencies
COPY requirements.txt /app/
RUN pip install --no-cache-dir -r requirements.txt

# Copy the project files into the container
COPY . /app/

# Make start.sh executable (ya no crear el script aquí, usar el archivo externo)
RUN chmod +x /app/start.sh

# Expose the port the app runs on
EXPOSE 8080

# Start the Django application with the external script
CMD ["/app/start.sh"]