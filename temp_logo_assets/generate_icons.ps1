# ShivaayMusic Icon Generation Script
# Created by: Shivaay (Shivaay20005)
# Copyright © 2024 Shivaay20005

# Define icon sizes for different densities
$iconSizes = @{
    "mdpi" = 48
    "hdpi" = 72
    "xhdpi" = 96
    "xxhdpi" = 144
    "xxxhdpi" = 192
}

# Source directory
$sourceDir = "c:\Users\shiva\Documents\Shivaay__Music\ShivaayMusic-master\temp_logo_assets"
$projectDir = "c:\Users\shiva\Documents\Shivaay__Music\ShivaayMusic-master\app\src\main\res"

Write-Host "ShivaayMusic Icon Generation Script" -ForegroundColor Green
Write-Host "Creating PNG icons from SVG sources..." -ForegroundColor Yellow

# Check if ImageMagick is available
try {
    $magickVersion = magick -version 2>$null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "ImageMagick found. Using ImageMagick for conversion." -ForegroundColor Green
        $useImageMagick = $true
    } else {
        $useImageMagick = $false
    }
} catch {
    $useImageMagick = $false
}

if (-not $useImageMagick) {
    Write-Host "ImageMagick not found. Please install ImageMagick or convert SVG files manually." -ForegroundColor Red
    Write-Host "Download from: https://imagemagick.org/script/download.php#windows" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Manual conversion instructions:" -ForegroundColor Cyan
    Write-Host "1. Use online SVG to PNG converters like:" -ForegroundColor White
    Write-Host "   - https://convertio.co/svg-png/" -ForegroundColor White
    Write-Host "   - https://cloudconvert.com/svg-to-png" -ForegroundColor White
    Write-Host "2. Convert each SVG file to the following sizes:" -ForegroundColor White
    
    foreach ($density in $iconSizes.Keys) {
        $size = $iconSizes[$density]
        Write-Host "   - mipmap-$density : ${size}x${size} px" -ForegroundColor White
    }
    
    Write-Host ""
    Write-Host "Files to convert:" -ForegroundColor Cyan
    Write-Host "- ic_launcher_background.svg" -ForegroundColor White
    Write-Host "- ic_launcher_foreground.svg" -ForegroundColor White  
    Write-Host "- ic_launcher_monochrome.svg" -ForegroundColor White
    Write-Host "- ic_launcher_legacy.svg (rename to ic_launcher.png)" -ForegroundColor White
    
    return
}

# Create directories and convert icons
foreach ($density in $iconSizes.Keys) {
    $size = $iconSizes[$density]
    $targetDir = "$projectDir\mipmap-$density"
    
    Write-Host "Creating icons for $density (${size}x${size})..." -ForegroundColor Yellow
    
    # Ensure target directory exists
    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }
    
    # Convert background
    magick "$sourceDir\ic_launcher_background.svg" -resize "${size}x${size}" "$targetDir\ic_launcher_background.png"
    
    # Convert foreground  
    magick "$sourceDir\ic_launcher_foreground.svg" -resize "${size}x${size}" "$targetDir\ic_launcher_foreground.png"
    
    # Convert monochrome
    magick "$sourceDir\ic_launcher_monochrome.svg" -resize "${size}x${size}" "$targetDir\ic_launcher_monochrome.png"
    
    # Convert legacy (resize legacy SVG to appropriate size)
    magick "$sourceDir\ic_launcher_legacy.svg" -resize "${size}x${size}" "$targetDir\ic_launcher.png"
}

Write-Host "Icon generation completed!" -ForegroundColor Green
Write-Host "New icons have been created in all mipmap directories." -ForegroundColor Green