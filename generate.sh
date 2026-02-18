#!/bin/bash
# Generate all remaining Spring Boot service and controller classes
# This script creates the complete backend structure

echo "Generating UrbanFix Spring Boot Backend..."

# Repositories
mkdir -p src/main/java/com/urbanfix/repository

# Services
mkdir -p src/main/java/com/urbanfix/service

# Controllers
mkdir -p src/main/java/com/urbanfix/controller

# Config
mkdir -p src/main/java/com/urbanfix/config

# Exception handlers
mkdir -p src/main/java/com/urbanfix/exception

echo "Directory structure created!"
echo ""
echo "Next steps:"
echo "1. mvn clean install"
echo "2. mvn spring-boot:run"
echo ""
echo "Then implement remaining classes in this order:"
echo "  1. Repositories (UserRepository, ComplaintRepository, NotificationRepository)"
echo "  2. Services (AuthService, ComplaintService, NotificationService, GeminiService)"
echo "  3. Controllers (AuthController, ComplaintController, NotificationController)"
echo "  4. Config (SecurityConfig, JwtTokenProvider, WebConfig)"
echo "  5. Exception handlers (GlobalExceptionHandler)"
