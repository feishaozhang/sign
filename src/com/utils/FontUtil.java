/*    */ package com.utils;
/*    */ 
/*    */

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/*    */
/*    */
/*    */
/*    */
/*    */ 
/*    */ public class FontUtil
/*    */ {
/*    */   public static Font getFont(float size, File fontFile, int type)
/*    */   {
/*    */     try
/*    */     {
/* 43 */       if (!fontFile.exists()) {
/* 45 */         return null;
/*    */       }
/* 47 */       Font dynamicFont = Font.createFont(0, fontFile);
/* 48 */       dynamicFont = dynamicFont.deriveFont(size);
/* 49 */       return dynamicFont.deriveFont(type);
/*    */     }
/*    */     catch (FontFormatException ex) {
/*    */     } catch (IOException ex) {
/*    */     }
/* 56 */     return null;
/*    */   }
	/**
	 * getFont By fileStream		
	 * @param size
	 * @param fontFileStream
	 * @param type
	 * @return
	 */
	public static Font getFont(float size, InputStream fontFileStream, int type)
/*    */   {
/*    */     try
/*    */     {
/* 43 */       if (fontFileStream == null) {
/* 45 */         return null;
/*    */       }
/* 47 */       Font dynamicFont = Font.createFont(0, fontFileStream);
/* 48 */       dynamicFont = dynamicFont.deriveFont(size);
/* 49 */       return dynamicFont.deriveFont(type);
   }
    catch (FontFormatException ex) {
   } catch (IOException ex) {
   }
    return null;
  }
 }

/* Location:           C:\Users\an\Desktop\rpcServer.jar
 * Qualified Name:     FontUtil
 * JD-Core Version:    0.6.2
 */