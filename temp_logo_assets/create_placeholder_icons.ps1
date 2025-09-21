# Alternative Icon Creation Script using .NET Graphics
# Created by: Shivaay (Shivaay20005)
# Copyright © 2024 Shivaay20005

Add-Type -AssemblyName System.Drawing

# Define icon sizes
$iconSizes = @{
    "mdpi" = 48
    "hdpi" = 72
    "xhdpi" = 96
    "xxhdpi" = 144
    "xxxhdpi" = 192
}

$projectDir = "c:\Users\shiva\Documents\Shivaay__Music\ShivaayMusic-master\app\src\main\res"

Write-Host "Creating placeholder icons with ShivaayMusic branding..." -ForegroundColor Green

function Create-ColoredIcon {
    param(
        [int]$Size,
        [string]$FilePath,
        [string]$ColorHex,
        [string]$Type
    )
    
    $bitmap = New-Object System.Drawing.Bitmap($Size, $Size)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    try {
        # Set high quality
        $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
        
        if ($Type -eq "background") {
            # Light gradient background
            $brush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml("#f8f9fa"))
            $graphics.FillRectangle($brush, 0, 0, $Size, $Size)
            $brush.Dispose()
        }
        elseif ($Type -eq "foreground") {
            # Musical note design
            $centerX = $Size / 2
            $centerY = $Size / 2
            $radius = $Size * 0.3
            
            # Draw concentric circles
            $pen1 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#9c27b0"), 3)
            $pen2 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#e91e63"), 2)
            $pen3 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#ff9800"), 2)
            
            $graphics.DrawEllipse($pen1, $centerX - $radius, $centerY - $radius, $radius * 2, $radius * 2)
            $graphics.DrawEllipse($pen2, $centerX - $radius * 0.8, $centerY - $radius * 0.8, $radius * 1.6, $radius * 1.6)
            $graphics.DrawEllipse($pen3, $centerX - $radius * 0.6, $centerY - $radius * 0.6, $radius * 1.2, $radius * 1.2)
            
            # Draw musical note
            $brush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml("#ffc107"))
            $graphics.FillEllipse($brush, $centerX - $radius * 0.25, $centerY, $radius * 0.5, $radius * 0.5)
            $graphics.FillRectangle($brush, $centerX + $radius * 0.1, $centerY - $radius * 0.7, $radius * 0.1, $radius * 0.8)
            
            $pen1.Dispose()
            $pen2.Dispose()
            $pen3.Dispose()
            $brush.Dispose()
        }
        elseif ($Type -eq "monochrome") {
            # Monochrome musical note
            $centerX = $Size / 2
            $centerY = $Size / 2
            $radius = $Size * 0.3
            
            $pen = New-Object System.Drawing.Pen([System.Drawing.Color]::Black, 2)
            $brush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::Black)
            
            $graphics.DrawEllipse($pen, $centerX - $radius, $centerY - $radius, $radius * 2, $radius * 2)
            $graphics.DrawEllipse($pen, $centerX - $radius * 0.8, $centerY - $radius * 0.8, $radius * 1.6, $radius * 1.6)
            $graphics.FillEllipse($brush, $centerX - $radius * 0.25, $centerY, $radius * 0.5, $radius * 0.5)
            $graphics.FillRectangle($brush, $centerX + $radius * 0.1, $centerY - $radius * 0.7, $radius * 0.1, $radius * 0.8)
            
            $pen.Dispose()
            $brush.Dispose()
        }
        else {
            # Legacy complete icon
            $centerX = $Size / 2
            $centerY = $Size / 2
            $radius = $Size * 0.3
            
            # Background
            $bgBrush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml("#f8f9fa"))
            $graphics.FillRectangle($bgBrush, 0, 0, $Size, $Size)
            
            # Concentric circles
            $pen1 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#9c27b0"), 2)
            $pen2 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#e91e63"), 2)
            $pen3 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#ff9800"), 1)
            
            $graphics.DrawEllipse($pen1, $centerX - $radius, $centerY - $radius, $radius * 2, $radius * 2)
            $graphics.DrawEllipse($pen2, $centerX - $radius * 0.8, $centerY - $radius * 0.8, $radius * 1.6, $radius * 1.6)
            $graphics.DrawEllipse($pen3, $centerX - $radius * 0.6, $centerY - $radius * 0.6, $radius * 1.2, $radius * 1.2)
            
            # Musical note
            $noteBrush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml("#ffc107"))
            $graphics.FillEllipse($noteBrush, $centerX - $radius * 0.25, $centerY, $radius * 0.5, $radius * 0.5)
            $graphics.FillRectangle($noteBrush, $centerX + $radius * 0.1, $centerY - $radius * 0.7, $radius * 0.1, $radius * 0.8)
            
            $bgBrush.Dispose()
            $pen1.Dispose()
            $pen2.Dispose()
            $pen3.Dispose()
            $noteBrush.Dispose()
        }
        
        $bitmap.Save($FilePath, [System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "Created: $FilePath" -ForegroundColor Green
    }
    finally {
        $graphics.Dispose()
        $bitmap.Dispose()
    }
}

# Create icons for each density
foreach ($density in $iconSizes.Keys) {
    $size = $iconSizes[$density]
    $targetDir = "$projectDir\mipmap-$density"
    
    Write-Host "Creating icons for $density (${size}x${size})..." -ForegroundColor Yellow
    
    # Ensure target directory exists
    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }
    
    # Create each icon type
    Create-ColoredIcon -Size $size -FilePath "$targetDir\ic_launcher_background.png" -ColorHex "#f8f9fa" -Type "background"
    Create-ColoredIcon -Size $size -FilePath "$targetDir\ic_launcher_foreground.png" -ColorHex "#ffc107" -Type "foreground"
    Create-ColoredIcon -Size $size -FilePath "$targetDir\ic_launcher_monochrome.png" -ColorHex "#000000" -Type "monochrome"
    Create-ColoredIcon -Size $size -FilePath "$targetDir\ic_launcher.png" -ColorHex "#ffc107" -Type "legacy"
}

Write-Host ""
Write-Host "Placeholder icons created successfully!" -ForegroundColor Green
Write-Host "Note: These are basic placeholder icons based on your design." -ForegroundColor Yellow
Write-Host "For best results, convert the SVG files manually using online tools." -ForegroundColor Yellow