# Enhanced ShivaayMusic Icon Generator
# Created by: Shivaay (Shivaay20005)
# Copyright © 2024 Shivaay20005

Add-Type -AssemblyName System.Drawing
Add-Type -AssemblyName System.Drawing.Drawing2D

$iconSizes = @{
    "mdpi" = 48
    "hdpi" = 72
    "xhdpi" = 96
    "xxhdpi" = 144
    "xxxhdpi" = 192
}

$projectDir = "c:\Users\shiva\Documents\Shivaay__Music\ShivaayMusic-master\app\src\main\res"

Write-Host "Creating enhanced ShivaayMusic icons based on your exact design..." -ForegroundColor Green

function Create-EnhancedIcon {
    param(
        [int]$Size,
        [string]$FilePath,
        [string]$Type
    )
    
    $bitmap = New-Object System.Drawing.Bitmap($Size, $Size)
    $graphics = [System.Drawing.Graphics]::FromImage($bitmap)
    
    try {
        $graphics.SmoothingMode = [System.Drawing.Drawing2D.SmoothingMode]::AntiAlias
        $graphics.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
        $graphics.CompositingQuality = [System.Drawing.Drawing2D.CompositingQuality]::HighQuality
        
        $centerX = $Size / 2
        $centerY = $Size / 2
        
        if ($Type -eq "background") {
            # Clean white background with subtle gradient
            $bgBrush = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
                [System.Drawing.Point]::new(0, 0),
                [System.Drawing.Point]::new($Size, $Size),
                [System.Drawing.ColorTranslator]::FromHtml("#ffffff"),
                [System.Drawing.ColorTranslator]::FromHtml("#f8f9fa")
            )
            $graphics.FillRectangle($bgBrush, 0, 0, $Size, $Size)
            
            # Add triangular decorative elements
            $triangleSize = $Size * 0.08
            $positions = @(
                @{X=0.2; Y=0.15; Color="#9c27b0"},
                @{X=0.85; Y=0.2; Color="#673ab7"},
                @{X=0.15; Y=0.75; Color="#e91e63"},
                @{X=0.9; Y=0.8; Color="#ff9800"},
                @{X=0.75; Y=0.15; Color="#ffc107"},
                @{X=0.1; Y=0.45; Color="#e91e63"},
                @{X=0.9; Y=0.55; Color="#9c27b0"},
                @{X=0.25; Y=0.9; Color="#ff9800"}
            )
            
            foreach ($pos in $positions) {
                $x = $Size * $pos.X
                $y = $Size * $pos.Y
                $brush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml($pos.Color))
                $brush.Color = [System.Drawing.Color]::FromArgb(100, $brush.Color)
                
                $points = @(
                    [System.Drawing.Point]::new($x, $y),
                    [System.Drawing.Point]::new($x + $triangleSize, $y),
                    [System.Drawing.Point]::new($x + $triangleSize/2, $y + $triangleSize)
                )
                $graphics.FillPolygon($brush, $points)
                $brush.Dispose()
            }
            
            $bgBrush.Dispose()
        }
        elseif ($Type -eq "foreground") {
            # Main musical note with concentric circles
            $radius1 = $Size * 0.35  # Outer ring
            $radius2 = $Size * 0.28  # Middle ring  
            $radius3 = $Size * 0.2   # Inner ring
            $noteRadius = $Size * 0.09 # Note head
            
            # Draw concentric circles with gradients
            # Outer purple ring
            $pen1 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#9c27b0"), $Size * 0.04)
            $graphics.DrawEllipse($pen1, $centerX - $radius1, $centerY - $radius1, $radius1 * 2, $radius1 * 2)
            
            # Middle pink ring
            $pen2 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#e91e63"), $Size * 0.035)
            $graphics.DrawEllipse($pen2, $centerX - $radius2, $centerY - $radius2, $radius2 * 2, $radius2 * 2)
            
            # Inner orange ring
            $pen3 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#ff9800"), $Size * 0.03)
            $graphics.DrawEllipse($pen3, $centerX - $radius3, $centerY - $radius3, $radius3 * 2, $radius3 * 2)
            
            # Musical note stem
            $stemBrush = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
                [System.Drawing.Point]::new($centerX, $centerY - $radius3),
                [System.Drawing.Point]::new($centerX, $centerY + $noteRadius),
                [System.Drawing.ColorTranslator]::FromHtml("#ffc107"),
                [System.Drawing.ColorTranslator]::FromHtml("#ff9800")
            )
            $stemWidth = $Size * 0.04
            $graphics.FillRectangle($stemBrush, $centerX + $noteRadius * 0.3, $centerY - $radius3, $stemWidth, $radius3 + $noteRadius)
            
            # Musical note head (circle)
            $noteBrush = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
                [System.Drawing.Point]::new($centerX - $noteRadius, $centerY),
                [System.Drawing.Point]::new($centerX + $noteRadius, $centerY + $noteRadius),
                [System.Drawing.ColorTranslator]::FromHtml("#ffc107"),
                [System.Drawing.ColorTranslator]::FromHtml("#ff8f00")
            )
            $graphics.FillEllipse($noteBrush, $centerX - $noteRadius, $centerY, $noteRadius * 2, $noteRadius * 2)
            
            # Musical note flag
            $flagBrush = [System.Drawing.Drawing2D.LinearGradientBrush]::new(
                [System.Drawing.Point]::new($centerX, $centerY - $radius3),
                [System.Drawing.Point]::new($centerX + $radius3 * 0.7, $centerY - $radius3 + $radius3 * 0.3),
                [System.Drawing.ColorTranslator]::FromHtml("#e91e63"),
                [System.Drawing.ColorTranslator]::FromHtml("#9c27b0")
            )
            
            $flagPoints = @(
                [System.Drawing.Point]::new($centerX + $noteRadius * 0.3 + $stemWidth, $centerY - $radius3),
                [System.Drawing.Point]::new($centerX + $radius3 * 0.7, $centerY - $radius3 + $radius3 * 0.1),
                [System.Drawing.Point]::new($centerX + $radius3 * 0.6, $centerY - $radius3 + $radius3 * 0.3),
                [System.Drawing.Point]::new($centerX + $noteRadius * 0.3 + $stemWidth, $centerY - $radius3 + $radius3 * 0.2)
            )
            $graphics.FillPolygon($flagBrush, $flagPoints)
            
            # Cleanup
            $pen1.Dispose()
            $pen2.Dispose()
            $pen3.Dispose()
            $stemBrush.Dispose()
            $noteBrush.Dispose()
            $flagBrush.Dispose()
        }
        elseif ($Type -eq "monochrome") {
            # Monochrome version - single color
            $radius1 = $Size * 0.35
            $radius2 = $Size * 0.28
            $radius3 = $Size * 0.2
            $noteRadius = $Size * 0.09
            
            $pen = New-Object System.Drawing.Pen([System.Drawing.Color]::Black, $Size * 0.03)
            $brush = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::Black)
            
            $graphics.DrawEllipse($pen, $centerX - $radius1, $centerY - $radius1, $radius1 * 2, $radius1 * 2)
            $graphics.DrawEllipse($pen, $centerX - $radius2, $centerY - $radius2, $radius2 * 2, $radius2 * 2)
            $graphics.DrawEllipse($pen, $centerX - $radius3, $centerY - $radius3, $radius3 * 2, $radius3 * 2)
            
            $stemWidth = $Size * 0.04
            $graphics.FillRectangle($brush, $centerX + $noteRadius * 0.3, $centerY - $radius3, $stemWidth, $radius3 + $noteRadius)
            $graphics.FillEllipse($brush, $centerX - $noteRadius, $centerY, $noteRadius * 2, $noteRadius * 2)
            
            $pen.Dispose()
            $brush.Dispose()
        }
        else {
            # Legacy complete icon (background + foreground combined)
            # Background
            $bgBrush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml("#f8f9fa"))
            $graphics.FillRectangle($bgBrush, 0, 0, $Size, $Size)
            
            # Add all foreground elements
            $radius1 = $Size * 0.35
            $radius2 = $Size * 0.28
            $radius3 = $Size * 0.2
            $noteRadius = $Size * 0.09
            
            $pen1 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#9c27b0"), $Size * 0.03)
            $pen2 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#e91e63"), $Size * 0.025)
            $pen3 = New-Object System.Drawing.Pen([System.Drawing.ColorTranslator]::FromHtml("#ff9800"), $Size * 0.02)
            
            $graphics.DrawEllipse($pen1, $centerX - $radius1, $centerY - $radius1, $radius1 * 2, $radius1 * 2)
            $graphics.DrawEllipse($pen2, $centerX - $radius2, $centerY - $radius2, $radius2 * 2, $radius2 * 2)
            $graphics.DrawEllipse($pen3, $centerX - $radius3, $centerY - $radius3, $radius3 * 2, $radius3 * 2)
            
            $noteBrush = New-Object System.Drawing.SolidBrush([System.Drawing.ColorTranslator]::FromHtml("#ffc107"))
            $stemWidth = $Size * 0.03
            $graphics.FillRectangle($noteBrush, $centerX + $noteRadius * 0.3, $centerY - $radius3, $stemWidth, $radius3 + $noteRadius)
            $graphics.FillEllipse($noteBrush, $centerX - $noteRadius, $centerY, $noteRadius * 2, $noteRadius * 2)
            
            $bgBrush.Dispose()
            $pen1.Dispose()
            $pen2.Dispose()
            $pen3.Dispose()
            $noteBrush.Dispose()
        }
        
        $bitmap.Save($FilePath, [System.Drawing.Imaging.ImageFormat]::Png)
        Write-Host "Created enhanced: $FilePath" -ForegroundColor Green
    }
    finally {
        $graphics.Dispose()
        $bitmap.Dispose()
    }
}

# Create enhanced icons for each density
foreach ($density in $iconSizes.Keys) {
    $size = $iconSizes[$density]
    $targetDir = "$projectDir\mipmap-$density"
    
    Write-Host "Creating enhanced icons for $density (${size}x${size})..." -ForegroundColor Yellow
    
    if (-not (Test-Path $targetDir)) {
        New-Item -ItemType Directory -Path $targetDir -Force | Out-Null
    }
    
    Create-EnhancedIcon -Size $size -FilePath "$targetDir\ic_launcher_background.png" -Type "background"
    Create-EnhancedIcon -Size $size -FilePath "$targetDir\ic_launcher_foreground.png" -Type "foreground"
    Create-EnhancedIcon -Size $size -FilePath "$targetDir\ic_launcher_monochrome.png" -Type "monochrome"
    Create-EnhancedIcon -Size $size -FilePath "$targetDir\ic_launcher.png" -Type "legacy"
}

Write-Host ""
Write-Host "Enhanced ShivaayMusic icons created successfully!" -ForegroundColor Green
Write-Host "The new icons should now match your exact design much better!" -ForegroundColor Green