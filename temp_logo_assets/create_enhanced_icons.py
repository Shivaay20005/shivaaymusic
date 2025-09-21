# ShivaayMusic Enhanced Icon Generator - Python Version
# Created by: Shivaay (Shivaay20005)
# Copyright © 2024 Shivaay20005

import os
from PIL import Image, ImageDraw, ImageFilter
import math

# Icon sizes for different densities
ICON_SIZES = {
    "mdpi": 48,
    "hdpi": 72,
    "xhdpi": 96,
    "xxhdpi": 144,
    "xxxhdpi": 192
}

PROJECT_DIR = r"c:\Users\shiva\Documents\Shivaay__Music\ShivaayMusic-master\app\src\main\res"

def create_gradient_circle(draw, center, radius, colors, width=5):
    """Create a gradient circle by drawing multiple circles with varying opacity"""
    steps = len(colors)
    for i, color in enumerate(colors):
        alpha = int(255 * (1 - i * 0.1))  # Varying opacity
        temp_color = (*color, alpha) if len(color) == 3 else color
        draw.ellipse([center[0] - radius, center[1] - radius, 
                     center[0] + radius, center[1] + radius], 
                    outline=temp_color, width=width)

def create_enhanced_background(size):
    """Create background with triangular decorative elements"""
    img = Image.new('RGBA', (size, size), (248, 249, 250, 255))
    draw = ImageDraw.Draw(img)
    
    # Triangular decorative elements
    triangle_size = size * 0.08
    positions = [
        (0.2, 0.15, (156, 39, 176)),   # Purple
        (0.85, 0.2, (103, 58, 183)),   # Deep purple
        (0.15, 0.75, (233, 30, 99)),   # Pink
        (0.9, 0.8, (255, 152, 0)),     # Orange
        (0.75, 0.15, (255, 193, 7)),   # Yellow
        (0.1, 0.45, (233, 30, 99)),    # Pink
        (0.9, 0.55, (156, 39, 176)),   # Purple
        (0.25, 0.9, (255, 152, 0))     # Orange
    ]
    
    for pos_x, pos_y, color in positions:
        x = size * pos_x
        y = size * pos_y
        points = [
            (x, y),
            (x + triangle_size, y),
            (x + triangle_size/2, y + triangle_size)
        ]
        # Add transparency
        color_with_alpha = (*color, 100)
        draw.polygon(points, fill=color_with_alpha)
    
    return img

def create_enhanced_foreground(size):
    """Create foreground with musical note and concentric circles"""
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = size // 2, size // 2
    
    # Concentric circles with your exact colors
    radius1 = size * 0.35  # Outer purple ring
    radius2 = size * 0.28  # Middle pink ring
    radius3 = size * 0.2   # Inner orange ring
    
    # Draw circles with proper colors and thickness
    line_width = max(2, size // 25)
    
    # Outer purple ring
    draw.ellipse([center_x - radius1, center_y - radius1, 
                 center_x + radius1, center_y + radius1], 
                outline=(156, 39, 176, 230), width=line_width)
    
    # Middle pink ring  
    draw.ellipse([center_x - radius2, center_y - radius2,
                 center_x + radius2, center_y + radius2],
                outline=(233, 30, 99, 230), width=line_width-1)
    
    # Inner orange ring
    draw.ellipse([center_x - radius3, center_y - radius3,
                 center_x + radius3, center_y + radius3],
                outline=(255, 152, 0, 230), width=line_width-1)
    
    # Musical note components
    note_radius = size * 0.09
    stem_width = max(2, size // 30)
    stem_height = radius3 + note_radius
    
    # Note stem
    stem_x = center_x + note_radius * 0.3
    stem_y = center_y - radius3
    draw.rectangle([stem_x, stem_y, stem_x + stem_width, stem_y + stem_height],
                  fill=(255, 193, 7, 255))
    
    # Note head (circle)
    note_x = center_x - note_radius
    note_y = center_y
    draw.ellipse([note_x, note_y, note_x + note_radius * 2, note_y + note_radius * 2],
                fill=(255, 193, 7, 255))
    
    # Note flag
    flag_points = [
        (stem_x + stem_width, stem_y),
        (stem_x + radius3 * 0.7, stem_y + radius3 * 0.1),
        (stem_x + radius3 * 0.6, stem_y + radius3 * 0.3),
        (stem_x + stem_width, stem_y + radius3 * 0.2)
    ]
    draw.polygon(flag_points, fill=(233, 30, 99, 200))
    
    return img

def create_enhanced_monochrome(size):
    """Create monochrome version"""
    img = Image.new('RGBA', (size, size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(img)
    
    center_x, center_y = size // 2, size // 2
    radius1 = size * 0.35
    radius2 = size * 0.28
    radius3 = size * 0.2
    
    line_width = max(2, size // 25)
    
    # Draw circles in black
    draw.ellipse([center_x - radius1, center_y - radius1,
                 center_x + radius1, center_y + radius1],
                outline=(0, 0, 0, 255), width=line_width)
    draw.ellipse([center_x - radius2, center_y - radius2,
                 center_x + radius2, center_y + radius2],
                outline=(0, 0, 0, 255), width=line_width-1)
    draw.ellipse([center_x - radius3, center_y - radius3,
                 center_x + radius3, center_y + radius3],
                outline=(0, 0, 0, 255), width=line_width-1)
    
    # Musical note in black
    note_radius = size * 0.09
    stem_width = max(2, size // 30)
    stem_height = radius3 + note_radius
    stem_x = center_x + note_radius * 0.3
    stem_y = center_y - radius3
    
    draw.rectangle([stem_x, stem_y, stem_x + stem_width, stem_y + stem_height],
                  fill=(0, 0, 0, 255))
    
    note_x = center_x - note_radius
    note_y = center_y
    draw.ellipse([note_x, note_y, note_x + note_radius * 2, note_y + note_radius * 2],
                fill=(0, 0, 0, 255))
    
    return img

def create_enhanced_legacy(size):
    """Create legacy complete icon"""
    # Start with background
    img = create_enhanced_background(size)
    
    # Add foreground elements
    foreground = create_enhanced_foreground(size)
    img = Image.alpha_composite(img, foreground)
    
    return img

def main():
    print("Creating enhanced ShivaayMusic icons...")
    
    try:
        for density, size in ICON_SIZES.items():
            target_dir = os.path.join(PROJECT_DIR, f"mipmap-{density}")
            
            print(f"Creating enhanced icons for {density} ({size}x{size})...")
            
            # Ensure target directory exists
            os.makedirs(target_dir, exist_ok=True)
            
            # Create each icon type
            bg_img = create_enhanced_background(size)
            bg_img.save(os.path.join(target_dir, "ic_launcher_background.png"))
            
            fg_img = create_enhanced_foreground(size)
            fg_img.save(os.path.join(target_dir, "ic_launcher_foreground.png"))
            
            mono_img = create_enhanced_monochrome(size)
            mono_img.save(os.path.join(target_dir, "ic_launcher_monochrome.png"))
            
            legacy_img = create_enhanced_legacy(size)
            legacy_img.save(os.path.join(target_dir, "ic_launcher.png"))
            
            print(f"✓ Created icons for {density}")
        
        print("\n✅ Enhanced ShivaayMusic icons created successfully!")
        print("🎵 Your new logo should now match your design much better!")
        
    except Exception as e:
        print(f"❌ Error: {e}")
        print("Please install Pillow: pip install Pillow")

if __name__ == "__main__":
    main()