/* CVS $Id: $ */
package uk.ac.osswatch.simal.model; 
/*
 * 
Copyright 2007 University of Oxford * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
 
/**
 * Vocabulary definitions from http://usefulinc.com/ns/doap# 
 * @author Auto-generated by schemagen on 20 Jul 2008 04:04 
 */
public class Doap {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://usefulinc.com/ns/doap#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /** <p>Repositorio para acceso an�nimo.Repository for anonymous access.D�p�t pour 
     *  acc�s anonyme.</p>
     */
    public static final Property ANON_ROOT = m_model.createProperty( "http://usefulinc.com/ns/doap#anon-root" );
    
    /** <p>Interface web del repositorio.Web browser interface to repository.Interface 
     *  web au d�p�t.</p>
     */
    public static final Property BROWSE = m_model.createProperty( "http://usefulinc.com/ns/doap#browse" );
    
    /** <p>Bug tracker para un proyecto.Suivi des bugs pour un projet.Bug tracker for 
     *  a project.</p>
     */
    public static final Property BUG_DATABASE = m_model.createProperty( "http://usefulinc.com/ns/doap#bug-database" );
    
    /** <p>A category of project.Une cat�gorie de projet.Una categor�a de proyecto.</p> */
    public static final Property CATEGORY = m_model.createProperty( "http://usefulinc.com/ns/doap#category" );
    
    /** <p>Fecha en la que algo fue creado, en formato AAAA-MM-DD. e.g. 2004-04-05Date 
     *  when something was created, in YYYY-MM-DD form. e.g. 2004-04-05Date � laquelle 
     *  a �t� cr�� quelque chose, au format AAAA-MM-JJ (par ex. 2004-04-05)</p>
     */
    public static final Property CREATED = m_model.createProperty( "http://usefulinc.com/ns/doap#created" );
    
    /** <p>Descripci�n en texto plano de un proyecto, de 2 a 4 enunciados de longitud.Plain 
     *  text description of a project, of 2-4 sentences in length.Texte descriptif 
     *  d'un projet, long de 2 � 4 phrases.</p>
     */
    public static final Property DESCRIPTION = m_model.createProperty( "http://usefulinc.com/ns/doap#description" );
    
    /** <p>D�veloppeur pour le projet.Developer of software for the project.Desarrollador 
     *  de software para el proyecto.</p>
     */
    public static final Property DEVELOPER = m_model.createProperty( "http://usefulinc.com/ns/doap#developer" );
    
    /** <p>Contributor of documentation to the project.Collaborateur � la documentation 
     *  du projet.Proveedor de documentaci�n para el proyecto.</p>
     */
    public static final Property DOCUMENTER = m_model.createProperty( "http://usefulinc.com/ns/doap#documenter" );
    
    /** <p>Mirror of software download web page.Mirror de la p�gina web de descarga.Miroir 
     *  de la page de t�l�chargement du programme.</p>
     */
    public static final Property DOWNLOAD_MIRROR = m_model.createProperty( "http://usefulinc.com/ns/doap#download-mirror" );
    
    /** <p>Web page from which the project software can be downloaded.Page web � partir 
     *  de laquelle on peut t�l�charger le programme.P�gina web de la cu�l se puede 
     *  bajar el software.</p>
     */
    public static final Property DOWNLOAD_PAGE = m_model.createProperty( "http://usefulinc.com/ns/doap#download-page" );
    
    /** <p>URI of download associated with this release.</p> */
    public static final Property FILE_RELEASE = m_model.createProperty( "http://usefulinc.com/ns/doap#file-release" );
    
    /** <p>Project contributor.Colaborador del proyecto.Collaborateur au projet.</p> */
    public static final Property HELPER = m_model.createProperty( "http://usefulinc.com/ns/doap#helper" );
    
    /** <p>URL of a project's homepage, associated with exactly one project.L'URL de 
     *  la page web d'un projet, associ�e avec un unique projet.El URL de la p�gina 
     *  de un proyecto, asociada con exactamente un proyecto.</p>
     */
    public static final Property HOMEPAGE = m_model.createProperty( "http://usefulinc.com/ns/doap#homepage" );
    
    /** <p>The URI of an RDF description of the license the software is distributed under.El 
     *  URI de una descripci�n RDF de la licencia bajo la cu�l se distribuye el software.L'URI 
     *  d'une description RDF de la licence sous laquelle le programme est distribu�.</p>
     */
    public static final Property LICENSE = m_model.createProperty( "http://usefulinc.com/ns/doap#license" );
    
    /** <p>Emplacement d'un d�p�t.lugar de un repositorio.Location of a repository.</p> */
    public static final Property LOCATION = m_model.createProperty( "http://usefulinc.com/ns/doap#location" );
    
    /** <p>Mailing list home page or email address.P�gina web de la lista de correo o 
     *  direcci�n de correo.Page web de la liste de diffusion, ou adresse de courriel.</p>
     */
    public static final Property MAILING_LIST = m_model.createProperty( "http://usefulinc.com/ns/doap#mailing-list" );
    
    /** <p>Desarrollador principal de un proyecto, un l�der de proyecto.Maintainer of 
     *  a project, a project leader.D�veloppeur principal d'un projet, un meneur du 
     *  projet.</p>
     */
    public static final Property MAINTAINER = m_model.createProperty( "http://usefulinc.com/ns/doap#maintainer" );
    
    /** <p>Nombre del m�dulo de un repositorio CVS, BitKeeper o Arch.Nom du module d'un 
     *  d�p�t CVS, BitKeeper ou Arch.Module name of a CVS, BitKeeper or Arch repository.</p>
     */
    public static final Property MODULE = m_model.createProperty( "http://usefulinc.com/ns/doap#module" );
    
    /** <p>El nombre de algo.Le nom de quelque chose.A name of something.</p> */
    public static final Property NAME = m_model.createProperty( "http://usefulinc.com/ns/doap#name" );
    
    /** <p>URL of a project's past homepage, associated with exactly one project.L'URL 
     *  d'une ancienne page web d'un projet, associ�e avec un unique projet.El URL 
     *  de la antigua p�gina de un proyecto, asociada con exactamente un proyecto.</p>
     */
    public static final Property OLD_HOMEPAGE = m_model.createProperty( "http://usefulinc.com/ns/doap#old-homepage" );
    
    /** <p>Operating system that a project is limited to. Omit this property if the project 
     *  is not OS-specific.Sistema opertivo al cu�l est� limitado el proyecto. Omita 
     *  esta propiedad si el proyecto no es espec�fico de un sistema opertaivo en 
     *  particular.Syst�me d'exploitation auquel est limit� le projet. Omettez cette 
     *  propri�t� si le projet n'est pas limit� � un syst�me d'exploitation.</p>
     */
    public static final Property OS = m_model.createProperty( "http://usefulinc.com/ns/doap#os" );
    
    /** <p>Langage de programmation avec lequel un projet est impl�ment�, ou avec lequel 
     *  il est pr�vu de l'utiliser.Programming language a project is implemented in 
     *  or intended for use with.Lenguaje de programaci�n en el que un proyecto es 
     *  implementado o con el cu�l pretende usarse.</p>
     */
    public static final Property PROGRAMMING_LANGUAGE = m_model.createProperty( "http://usefulinc.com/ns/doap#programming-language" );
    
    /** <p>Un release (versi�n) de un proyecto.Une release (r�vision) d'un projet.A project 
     *  release.</p>
     */
    public static final Property RELEASE = m_model.createProperty( "http://usefulinc.com/ns/doap#release" );
    
    /** <p>Repositorio del c�digo fuente.Source code repository.D�p�t du code source.</p> */
    public static final Property REPOSITORY = m_model.createProperty( "http://usefulinc.com/ns/doap#repository" );
    
    /** <p>Identifiant de r�vision d'une release du programme.Indentificador de la versi�n 
     *  de un release de software.Revision identifier of a software release.</p>
     */
    public static final Property REVISION = m_model.createProperty( "http://usefulinc.com/ns/doap#revision" );
    
    /** <p>P�gina web con capturas de pantalla del proyecto.Web page with screenshots 
     *  of project.Page web avec des captures d'�cran du projet.</p>
     */
    public static final Property SCREENSHOTS = m_model.createProperty( "http://usefulinc.com/ns/doap#screenshots" );
    
    /** <p>Descripci�n corta (8 o 9 palabras) en texto plano de un proyecto.Short (8 
     *  or 9 words) plain text description of a project.Texte descriptif concis (8 
     *  ou 9 mots) d'un projet.</p>
     */
    public static final Property SHORTDESC = m_model.createProperty( "http://usefulinc.com/ns/doap#shortdesc" );
    
    /** <p>A tester or other quality control contributor.Un testeur ou un collaborateur 
     *  au contr�le qualit�.Un tester u otro proveedor de control de calidad.</p>
     */
    public static final Property TESTER = m_model.createProperty( "http://usefulinc.com/ns/doap#tester" );
    
    /** <p>Proveedor de traducciones al proyecto.Collaborateur � la traduction du projet.Contributor 
     *  of translations to the project.</p>
     */
    public static final Property TRANSLATOR = m_model.createProperty( "http://usefulinc.com/ns/doap#translator" );
    
    /** <p>L'URL du Wiki pour la discussion collaborative sur le projet.URL del Wiki 
     *  para discusi�n colaborativa del proyecto.URL of Wiki for collaborative discussion 
     *  of project.</p>
     */
    public static final Property WIKI = m_model.createProperty( "http://usefulinc.com/ns/doap#wiki" );
    
    /** <p>Repositorio GNU Arch del c�digo fuente.GNU Arch source code repository.D�p�t 
     *  GNU Arch du code source.</p>
     */
    public static final Resource ARCH_REPOSITORY = m_model.createResource( "http://usefulinc.com/ns/doap#ArchRepository" );
    
    /** <p>Repositorio BitKeeper del c�digo fuente.D�p�t BitKeeper du code source.BitKeeper 
     *  source code repository.</p>
     */
    public static final Resource BKREPOSITORY = m_model.createResource( "http://usefulinc.com/ns/doap#BKRepository" );
    
    /** <p>CVS source code repository.Repositorio CVS del c�digo fuente.D�p�t CVS du 
     *  code source.</p>
     */
    public static final Resource CVSREPOSITORY = m_model.createResource( "http://usefulinc.com/ns/doap#CVSRepository" );

    /** <p>SVN source code repository.Repositorio SVN del c�digo fuente.D�p�t SVN du 
     *  code source.</p>
     */
    public static final Resource SVNREPOSITORY = m_model.createResource( "http://usefulinc.com/ns/doap#SVNRepository" );
    
}
